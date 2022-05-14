package org.apache.pdfbox.pdmodel.font;

/**
 * @author trq
 * @version 1.0
 * @since 2022/5/14 12:42
 */
public final class FontMappers {
    private static FontMapper instance;

    public static FontMapper instance() {
        if (instance == null) {
            instance = DefaultFontMapper.INSTANCE;
        }
        // 每次刷新下字体缓存，保证设置的字体生效
        ((FontMapperImpl) instance).setReplaceFont();
        return instance;
    }

    public static synchronized void set(FontMapper fontMapper) {
        instance = fontMapper;
    }

    private static class DefaultFontMapper {
        private static final FontMapper INSTANCE = new FontMapperImpl();
    }
}
