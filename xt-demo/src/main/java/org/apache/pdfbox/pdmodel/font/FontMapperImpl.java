package org.apache.pdfbox.pdmodel.font;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fontbox.FontBoxFont;
import org.apache.fontbox.ttf.OpenTypeFont;
import org.apache.fontbox.ttf.TTFParser;
import org.apache.fontbox.ttf.TrueTypeFont;
import org.apache.fontbox.type1.Type1Font;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Font mapper, locates non-embedded fonts via a pluggable FontProvider.
 * pdf转图片如果乱码的话，修改这个类加上字体
 * @author trq
 * @version 1.0
 * @since 2022/5/14 12:44
 */
final class FontMapperImpl implements FontMapper {
    private static final Log log = LogFactory.getLog(FontMapperImpl.class);

    private static final FontCache fontCache = new FontCache();
    private FontProvider fontProvider;
    private Map<String, FontInfo> fontInfoByName;
    private final TrueTypeFont lastResortFont;
    /** Map of PostScript name substitutes, in priority order. */
    private final Map<String, List<String>> substitutes = new HashMap();

    FontMapperImpl() {
        // substitutes for standard 14 fonts
        this.substitutes.put("Courier", Arrays.asList("CourierNew", "CourierNewPSMT", "LiberationMono", "NimbusMonL-Regu"));
        this.substitutes.put("Courier-Bold", Arrays.asList("CourierNewPS-BoldMT", "CourierNew-Bold", "LiberationMono-Bold", "NimbusMonL-Bold"));
        this.substitutes.put("Courier-Oblique", Arrays.asList("CourierNewPS-ItalicMT", "CourierNew-Italic", "LiberationMono-Italic", "NimbusMonL-ReguObli"));
        this.substitutes.put("Courier-BoldOblique", Arrays.asList("CourierNewPS-BoldItalicMT", "CourierNew-BoldItalic", "LiberationMono-BoldItalic", "NimbusMonL-BoldObli"));
        this.substitutes.put("Helvetica", Arrays.asList("ArialMT", "Arial", "LiberationSans", "NimbusSanL-Regu"));
        this.substitutes.put("Helvetica-Bold", Arrays.asList("Arial-BoldMT", "Arial-Bold", "LiberationSans-Bold", "NimbusSanL-Bold"));
        this.substitutes.put("Helvetica-Oblique", Arrays.asList("Arial-ItalicMT", "Arial-Italic", "Helvetica-Italic", "LiberationSans-Italic", "NimbusSanL-ReguItal"));
        this.substitutes.put("Helvetica-BoldOblique", Arrays.asList("Arial-BoldItalicMT", "Helvetica-BoldItalic", "LiberationSans-BoldItalic", "NimbusSanL-BoldItal"));
        this.substitutes.put("Times-Roman", Arrays.asList("TimesNewRomanPSMT", "TimesNewRoman", "TimesNewRomanPS", "LiberationSerif", "NimbusRomNo9L-Regu"));
        this.substitutes.put("Times-Bold", Arrays.asList("TimesNewRomanPS-BoldMT", "TimesNewRomanPS-Bold", "TimesNewRoman-Bold", "LiberationSerif-Bold", "NimbusRomNo9L-Medi"));
        this.substitutes.put("Times-Italic", Arrays.asList("TimesNewRomanPS-ItalicMT", "TimesNewRomanPS-Italic", "TimesNewRoman-Italic", "LiberationSerif-Italic", "NimbusRomNo9L-ReguItal"));
        this.substitutes.put("Times-BoldItalic", Arrays.asList("TimesNewRomanPS-BoldItalicMT", "TimesNewRomanPS-BoldItalic", "TimesNewRoman-BoldItalic", "LiberationSerif-BoldItalic", "NimbusRomNo9L-MediItal"));
        this.substitutes.put("Symbol", Arrays.asList("Symbol", "SymbolMT", "StandardSymL"));
        this.substitutes.put("ZapfDingbats", Arrays.asList("ZapfDingbatsITC", "Dingbats", "MS-Gothic"));

        Iterator var1 = Standard14Fonts.getNames().iterator();
        while(var1.hasNext()) {
            String baseName = (String)var1.next();
            if (!this.substitutes.containsKey(baseName)) {
                String mappedName = Standard14Fonts.getMappedFontName(baseName);
                this.substitutes.put(baseName, this.copySubstitutes(mappedName));
            }
        }

        try {
            String ttfName = "/org/apache/pdfbox/resources/ttf/LiberationSans-Regular.ttf";
            InputStream ttfStream = FontMapper.class.getResourceAsStream(ttfName);
            if (ttfStream == null) {
                throw new IOException("Error loading resource: " + ttfName);
            } else {
                TTFParser ttfParser = new TTFParser();
                this.lastResortFont = ttfParser.parse(ttfStream);
            }
        } catch (IOException var4) {
            throw new RuntimeException(var4);
        }
    }

    /**
     * 从数据字典中获取配置替换的字体，接口调用
     * key为pdf中识别出的字体， value为识别不到时优先匹配的字体，可以匹配多个，排前面则优先匹配
     */
    public void setReplaceFont() {
        substitutes.put("STSong-Light",
                Arrays.asList("SimSun", "FangSong", "STSong", "STFangsong", "WenQuanYi Zen Hei"));
        substitutes.put("AdobeSongStd-Light",
                Arrays.asList("SimSun", "FangSong", "STSong", "STFangsong", "WenQuanYi Zen Hei"));
        substitutes.put("AdobeKaitiStd-Regular",
                Arrays.asList("SimSun", "FangSong", "STSong", "WenQuanYi Zen Hei"));
        substitutes.put("KaiTi_GB2312",
                Arrays.asList("SimSun", "FangSong", "STSong", "WenQuanYi Zen Hei"));
        substitutes.put("CourierNewPSMT",
                Arrays.asList("SimSun", "FangSong", "STSong", "WenQuanYi Zen Hei"));
        substitutes.put("KaiTi",
                Arrays.asList("SimSun", "FangSong", "STSong", "WenQuanYi Zen Hei"));
    }

    // lazy thread safe singleton
    private static class DefaultFontProvider {
        private static final FontProvider INSTANCE = new FileSystemFontProvider(fontCache);
    }

    /**
     * Sets the font service provider.
     * @param fontProvider
     */
    public synchronized void setProvider(FontProvider fontProvider) {
        this.fontInfoByName = this.createFontInfoByName(fontProvider.getFontInfo());
        this.fontProvider = fontProvider;
    }

    /**
     * Returns the font service provider. Defaults to using FileSystemFontProvider.
     * @return
     */
    public synchronized FontProvider getProvider() {
        if (this.fontProvider == null) {
            this.setProvider(DefaultFontProvider.INSTANCE);
        }
        return this.fontProvider;
    }

    /**
     * Returns the font cache associated with this FontMapper.
     * This method is needed by FontProvider subclasses.
     * @return
     */
    public FontCache getFontCache() {
        return fontCache;
    }

    private Map<String, FontInfo> createFontInfoByName(List<? extends FontInfo> fontInfoList) {
        Map<String, FontInfo> map = new LinkedHashMap();
        Iterator var3 = fontInfoList.iterator();

        while(var3.hasNext()) {
            FontInfo info = (FontInfo)var3.next();
            Iterator var5 = this.getPostScriptNames(info.getPostScriptName()).iterator();

            while(var5.hasNext()) {
                String name = (String)var5.next();
                map.put(name, info);
                this.log.debug("当前系统已存在字体：" + name);
            }
        }

        return map;
    }

    /**
     * Gets alternative names, as seen in some PDFs, e.g. PDFBOX-142.
     * @param postScriptName
     * @return
     */
    private Set<String> getPostScriptNames(String postScriptName) {
        Set<String> names = new HashSet();

        // built-in PostScript name
        names.add(postScriptName);
        // remove hyphens (e.g. Arial-Black -> ArialBlack)
        names.add(postScriptName.replace("-", ""));

        return names;
    }

    /**
     * Copies a list of font substitues, adding the original font at the start of the list.
     * @param postScriptName
     * @return
     */
    private List<String> copySubstitutes(String postScriptName) {
        return new ArrayList((Collection)this.substitutes.get(postScriptName));
    }

    /**
     * Adds a top-priority substitute for the given font.
     * @param match PostScript name of the font to match
     * @param replace PostScript name of the font to use as a replacement
     */
    public void addSubstitute(String match, String replace) {
        if (!this.substitutes.containsKey(match)) {
            this.substitutes.put(match, new ArrayList<String>());
        }

        ((List)this.substitutes.get(match)).add(replace);
    }

    /**
     * Retruns the substitutes for a given font.
     * @param postScriptName
     * @return
     */
    private List<String> getSubstitutes(String postScriptName) {
        List<String> subs = (List)this.substitutes.get(postScriptName.replace(" ", ""));
        return subs != null ? subs : Collections.emptyList();
    }

    /**
     * Attempts to find a good fallback based on the font descriptor.
     * @param fontDescriptor
     * @return
     */
    private String getFallbackFontName(PDFontDescriptor fontDescriptor) {
        String fontName;
        if (fontDescriptor != null) {
            // heuristic detection of bold
            boolean isBold = false;
            String name = fontDescriptor.getFontName();
            if (name != null) {
                String lower = fontDescriptor.getFontName().toLowerCase();
                isBold = lower.contains("bold")
                        || lower.contains("black")
                        || lower.contains("heavy");
            }

            // font descriptor flags should describe the style
            if (fontDescriptor.isFixedPitch()) {
                fontName = "Courier";
                if (isBold && fontDescriptor.isItalic()) {
                    fontName = fontName + "-BoldOblique";
                } else if (isBold) {
                    fontName = fontName + "-Bold";
                } else if (fontDescriptor.isItalic()) {
                    fontName = fontName + "-Oblique";
                }
            } else if (fontDescriptor.isSerif()) {
                fontName = "Times";
                if (isBold && fontDescriptor.isItalic()) {
                    fontName = fontName + "-BoldItalic";
                } else if (isBold) {
                    fontName = fontName + "-Bold";
                } else if (fontDescriptor.isItalic()) {
                    fontName = fontName + "-Italic";
                } else {
                    fontName = fontName + "-Roman";
                }
            } else {
                fontName = "Helvetica";
                if (isBold && fontDescriptor.isItalic()) {
                    fontName = fontName + "-BoldOblique";
                } else if (isBold) {
                    fontName = fontName + "-Bold";
                } else if (fontDescriptor.isItalic()) {
                    fontName = fontName + "-Oblique";
                }
            }
        } else {
            // if there is no FontDescriptor then we just fall back to Times Roman
            fontName = "Times-Roman";
        }

        return fontName;
    }

    /**
     * Finds a TrueType font with the given PostScript name, or a suitable substitute, or null.
     * @param baseFont
     * @param fontDescriptor
     * @return
     */
    @Override
    public FontMapping<TrueTypeFont> getTrueTypeFont(String baseFont, PDFontDescriptor fontDescriptor) {
        TrueTypeFont ttf = (TrueTypeFont)this.findFont(FontFormat.TTF, baseFont);
        if (ttf != null) {
            return new FontMapping(ttf, false);
        } else {
            String fontName = this.getFallbackFontName(fontDescriptor);
            ttf = (TrueTypeFont)this.findFont(FontFormat.TTF, fontName);
            if (ttf == null) {
                // we have to return something here as TTFs aren't strictly required on the system
                ttf = this.lastResortFont;
            }

            return new FontMapping<TrueTypeFont>(ttf, true);
        }
    }

    /**
     * Finds a font with the given PostScript name, or a suitable substitute, or null.
     * This allows any font to be sustituted with a PFB, TTF or OTF.
     * @param baseFont
     * @param fontDescriptor the FontDescriptor of the font to find
     * @return
     */
    @Override
    public FontMapping<FontBoxFont> getFontBoxFont(String baseFont, PDFontDescriptor fontDescriptor) {
        FontBoxFont font = this.findFontBoxFont(baseFont);
        if (font != null) {
            return new FontMapping(font, false);
        } else {
            String fallbackName = this.getFallbackFontName(fontDescriptor);
            font = this.findFontBoxFont(fallbackName);
            if (font == null) {
                // we have to return something here as TTFs aren't strictly required on the system
                font = this.lastResortFont;
            }

            return new FontMapping((FontBoxFont)font, true);
        }
    }

    /**
     * Finds a font with the given PostScript name, or a suitable substitute, or null.
     * @param postScriptName PostScript font name
     * @return
     */
    private FontBoxFont findFontBoxFont(String postScriptName) {
        Type1Font t1 = (Type1Font)this.findFont(FontFormat.PFB, postScriptName);
        if (t1 != null) {
            return t1;
        } else {
            TrueTypeFont ttf = (TrueTypeFont)this.findFont(FontFormat.TTF, postScriptName);
            if (ttf != null) {
                return ttf;
            } else {
                OpenTypeFont otf = (OpenTypeFont)this.findFont(FontFormat.OTF, postScriptName);
                return otf != null ? otf : null;
            }
        }
    }

    /**
     * Finds a font with the given PostScript name, or a suitable substitute, or null.
     * @param format
     * @param postScriptName PostScript font name
     * @return
     */
    private FontBoxFont findFont(FontFormat format, String postScriptName) {
        // handle damaged PDFs, see PDFBOX-2884
        if (postScriptName == null) {
            return null;
        } else {
            // make sure the font provider is initialized
            if (this.fontProvider == null) {
                this.getProvider();
            }

            // first try to match the PostScript name
            FontInfo info = this.getFont(format, postScriptName);
            if (info != null) {
                return info.getFont();
            } else {
                info = this.getFont(format, postScriptName.replace("-", ""));
                if (info != null) {
                    return info.getFont();
                }

                // then try named substitutes
                for (String substituteName : getSubstitutes(postScriptName)) {
                    info = this.getFont(format, substituteName);
                    if (info != null) {
                        return info.getFont();
                    }
                }

                // then try converting Windows names e.g. (ArialNarrow,Bold) -> (ArialNarrow-Bold)
                info = this.getFont(format, postScriptName.replaceAll(",", "-"));
                if (info != null) {
                    return info.getFont();
                }

                // try appending "-Regular", works for Wingdings on Windows
                info = this.getFont(format, postScriptName + "-Regular");
                if (info != null) {
                    return info.getFont();
                }

                // no matches
                return null;
            }
        }
    }

    /**
     * Finds the named font with the given format.
     * @param format
     * @param postScriptName
     * @return
     */
    private FontInfo getFont(FontFormat format, String postScriptName) {
        // strip subset tag (happens when we substitute a corrput embedded font, see PDFBOX-2642)
        if (postScriptName.contains("+")) {
            postScriptName = postScriptName.substring(postScriptName.indexOf('+') + 1);
        }

        // look up the PostScript name
        FontInfo info = (FontInfo)this.fontInfoByName.get(postScriptName);
        return info != null && info.getFormat() == format ? info : null;
    }

    /**
     * Finds a CFF CID-Keyed font with the given PostScript name, or a suitable substitute, or null.
     * This method can also map CJK fonts via their CIDSystemInfo (ROS).
     * @param baseFont
     * @param fontDescriptor FontDescriptor
     * @param cidSystemInfo the CID system info, e.g. "Adobe-Japan1", if any.
     * @return
     */
    @Override
    public CIDFontMapping getCIDFont(String baseFont, PDFontDescriptor fontDescriptor, PDCIDSystemInfo cidSystemInfo) {
        OpenTypeFont otf1 = (OpenTypeFont)this.findFont(FontFormat.OTF, baseFont);
        if (otf1 != null) {
            return new CIDFontMapping(otf1, (FontBoxFont)null, false);
        } else {
            TrueTypeFont ttf = (TrueTypeFont)this.findFont(FontFormat.TTF, baseFont);
            if (ttf != null) {
                return new CIDFontMapping((OpenTypeFont)null, ttf, false);
            } else {
                if (cidSystemInfo != null) {
                    String collection = cidSystemInfo.getRegistry() + "-" + cidSystemInfo.getOrdering();
                    if (collection.equals("Adobe-GB1") || collection.equals("Adobe-CNS1") || collection.equals("Adobe-Japan1") || collection.equals("Adobe-Korea1")) {
                        PriorityQueue<FontMatch> queue = this.getFontMatches(fontDescriptor, cidSystemInfo);
                        FontMatch bestMatch = (FontMatch)queue.poll();
                        if (bestMatch != null) {
                            FontBoxFont font = bestMatch.info.getFont();
                            if (font instanceof OpenTypeFont) {
                                return new CIDFontMapping((OpenTypeFont)font, (FontBoxFont)null, true);
                            } else {
                                return new CIDFontMapping((OpenTypeFont)null, font, true);
                            }
                        }
                    }
                }

                // last-resort fallback
                return new CIDFontMapping((OpenTypeFont)null, this.lastResortFont, true);
            }
        }
    }

    /**
     * Returns a list of matching fonts, scored by suitability. Positive scores indicate matches
     * for certain attirbutes, while negative scores indicate mismatches. Zero scores are neutral.
     * @param fontDescriptor FontDescriptor, always present
     * @param cidSystemInfo Font's CIDSystemInfo, may be null
     * @return
     */
    private PriorityQueue<FontMatch> getFontMatches(PDFontDescriptor fontDescriptor, PDCIDSystemInfo cidSystemInfo) {
        PriorityQueue<FontMatch> queue = new PriorityQueue(20);
        Iterator var4 = this.fontInfoByName.values().iterator();

        while(true) {
            FontMatch match;
            while(true) {
                FontInfo info;
                do {
                    if (!var4.hasNext()) {
                        return queue;
                    }

                    info = (FontInfo)var4.next();
                } while(cidSystemInfo != null && !this.isCharSetMatch(cidSystemInfo, info));

                match = new FontMatch(info);
                if (fontDescriptor.getPanose() != null && info.getPanose() != null) {
                    PDPanoseClassification panose = fontDescriptor.getPanose().getPanose();
                    if (panose.getFamilyKind() != info.getPanose().getFamilyKind()) {
                        break;
                    }

                    if (panose.getFamilyKind() == 0 && (info.getPostScriptName().toLowerCase().contains("barcode") || info.getPostScriptName().startsWith("Code")) && !this.probablyBarcodeFont(fontDescriptor)) {
                        continue;
                    }

                    if (panose.getSerifStyle() == info.getPanose().getSerifStyle()) {
                        match.score += 2.0D;
                    } else if (panose.getSerifStyle() >= 2 && panose.getSerifStyle() <= 5 && info.getPanose().getSerifStyle() >= 2 && info.getPanose().getSerifStyle() <= 5) {
                        ++match.score;
                    } else if (panose.getSerifStyle() >= 11 && panose.getSerifStyle() <= 13 && info.getPanose().getSerifStyle() >= 11 && info.getPanose().getSerifStyle() <= 13) {
                        ++match.score;
                    } else if (panose.getSerifStyle() != 0 && info.getPanose().getSerifStyle() != 0) {
                        --match.score;
                    }

                    int weight = info.getPanose().getWeight();
                    int weightClass = info.getWeightClassAsPanose();
                    if (Math.abs(weight - weightClass) > 2) {
                        weight = weightClass;
                    }

                    if (panose.getWeight() == weight) {
                        match.score += 2.0D;
                    } else if (panose.getWeight() > 1 && weight > 1) {
                        float dist = (float)Math.abs(panose.getWeight() - weight);
                        match.score += 1.0D - (double)dist * 0.5D;
                    }
                    break;
                }

                if (fontDescriptor.getFontWeight() > 0.0F && info.getWeightClass() > 0) {
                    float dist = Math.abs(fontDescriptor.getFontWeight() - (float)info.getWeightClass());
                    match.score += 1.0D - (double)(dist / 100.0F) * 0.5D;
                }
                break;
            }

            queue.add(match);
        }
    }

    private boolean probablyBarcodeFont(PDFontDescriptor fontDescriptor) {
        String ff = fontDescriptor.getFontFamily();
        if (ff == null) {
            ff = "";
        }

        String fn = fontDescriptor.getFontName();
        if (fn == null) {
            fn = "";
        }

        return ff.startsWith("Code") || ff.toLowerCase().contains("barcode") || fn.startsWith("Code") || fn.toLowerCase().contains("barcode");
    }

    private boolean isCharSetMatch(PDCIDSystemInfo cidSystemInfo, FontInfo info) {
        if (info.getCIDSystemInfo() != null) {
            return info.getCIDSystemInfo().getRegistry().equals(cidSystemInfo.getRegistry())
                    && info.getCIDSystemInfo().getOrdering().equals(cidSystemInfo.getOrdering());
        } else {
            long codePageRange = info.getCodePageRange();
            long JIS_JAPAN = 131072L;
            long CHINESE_SIMPLIFIED = 262144L;
            long KOREAN_WANSUNG = 524288L;
            long CHINESE_TRADITIONAL = 1048576L;
            long KOREAN_JOHAB = 2097152L;
            if (cidSystemInfo.getOrdering().equals("GB1")
                    && (codePageRange & CHINESE_SIMPLIFIED) == CHINESE_SIMPLIFIED) {
                return true;
            } else if (cidSystemInfo.getOrdering().equals("CNS1")
                    && (codePageRange & CHINESE_TRADITIONAL) == CHINESE_TRADITIONAL) {
                return true;
            } else if (cidSystemInfo.getOrdering().equals("Japan1")
                    && (codePageRange & JIS_JAPAN) == JIS_JAPAN) {
                return true;
            } else {
                return cidSystemInfo.getOrdering().equals("Korea1")
                        && (codePageRange & KOREAN_WANSUNG) == KOREAN_WANSUNG
                        || (codePageRange & KOREAN_JOHAB) == KOREAN_JOHAB;
            }
        }
    }

    /**
     * A potential match for a font substitution.
     */
    private static class FontMatch implements Comparable<FontMatch> {
        double score;
        final FontInfo info;

        FontMatch(FontInfo info) {
            this.info = info;
        }

        @Override
        public int compareTo(FontMatch match) {
            return Double.compare(match.score, this.score);
        }
    }

    /**
     * For debugging. Prints all matches and returns the best match.
     * @param queue
     * @return
     */
    private FontMatch printMatches(PriorityQueue<FontMatch> queue) {
        FontMatch bestMatch = (FontMatch)queue.peek();
        System.out.println("-------");

        while(!queue.isEmpty()) {
            FontMatch match = (FontMatch)queue.poll();
            FontInfo info = match.info;
            System.out.println(match.score + " | " + info.getMacStyle() + " "
                    + info.getFamilyClass() + " " + info.getPanose() + " "
                    + info.getCIDSystemInfo() + " " + info.getPostScriptName() + " "
                    + info.getFormat());
        }

        System.out.println("-------");
        return bestMatch;
    }
}
