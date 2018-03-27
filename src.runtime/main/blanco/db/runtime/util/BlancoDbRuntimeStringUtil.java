/*******************************************************************************
 * blancoDb Enterprise Edition
 * Copyright (C) 2004-2012 Toshiki Iga
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *******************************************************************************/
/*******************************************************************************
 * Copyright (c) 2004-2012 Toshiki IGA and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Toshiki IGA - initial implementation
 *******************************************************************************/
/*******************************************************************************
 * Copyright 2004-2012 Toshiki IGA and others.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package blanco.db.runtime.util;

public class BlancoDbRuntimeStringUtil {
    /**
     * あたえられた文字列について、Microsoft Windows 日本語版形式の String に変換します。
     * 
     * @param original
     *            入力文字列。
     * @return 変換後の文字列。
     */
    public static String convertToMsWindows31jUnicode(final String original) {
        if (original == null) {
            // null があたえられた場合には null を戻します。
            return null;
        }

        // 文字列が変更されたかどうか。
        boolean isModified = false;

        final char[] charArray = original.toCharArray();
        for (int index = 0; index < charArray.length; index++) {
            switch (charArray[index]) {
            case '\u2016':
                // From: DOUBLE VERTICAL LINE
                // To: PARALLEL TO
                // half width to full width.
                charArray[index] = '\u2225';
                isModified = true;
                break;
            case '\u2212':
                // From: MINUS SIGN
                // To: FULLWIDTH HYPHEN-MINUS
                // half width to full width.
                charArray[index] = '\uFF0D';
                isModified = true;
                break;
            }
        }

        if (isModified == false) {
            // 変更はありませんでした。そのまま返却します。
            return original;
        }

        // 変更があった場合のみ文字列を生成します。
        return new String(charArray);
    }
}
