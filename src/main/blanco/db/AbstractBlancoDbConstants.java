/*
 * blancoDb Enterprise Edition
 * Copyright (C) 2004-2012 Toshiki Iga
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.db;

import blanco.constants.BlancoConstantsVersion;
import blanco.gettersetter.BlancoGetterSetter;

/**
 * blancoDb のための定数クラス。
 * 
 * @author Toshiki IGA
 */
@BlancoConstantsVersion(prefix = "2.2.4-I")
public abstract class AbstractBlancoDbConstants {
    /**
     * プロダクト名。英字表現とします。
     */
    @BlancoGetterSetter(setter = false)
    public static final String PRODUCT_NAME = "blancoDb Enterprise Edition";

    /**
     * プロダクト名の小文字版。英字表現とします。
     */
    public static final String PRODUCT_NAME_LOWER = "blancodbee";

    /**
     * 処理の過程で利用されるサブディレクトリ。
     */
    public static final String TARGET_SUBDIRECTORY = "/db";
}
