package org.apache.pdfbox.pdmodel;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;

import java.io.IOException;

/**
 * @author trq
 * @version 1.0
 * @since 2022/5/14 14:05
 * 继承pdfbox原生DefaultResourceCache类重写put方法。
 *
 * 网上有说使用pdfbox导致内存溢出可能原因之一为：解析后的图片被存于
 * DefaultResourceCache中导致内存溢出，发现是SoftReference导致无法
 * 被回收，DefaultResourceCache.java里的put方法用了SoftReference，
 * 故写此类继承DefaultResourceCache重写public void put(COSObject indirect, PDXObject pdxObject)
 * 方法，然后调用PDDocument.setResourceCache(新写的子类)方法。
 */
@Slf4j
public class MyDefaultResourceCache extends DefaultResourceCache {

    @Override
    public void put(COSObject indirect, PDXObject pdxObject) throws IOException {
//        super.put(indirect, pdxObject);
    }
}
