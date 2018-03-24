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
package blanco.db.runtime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Interface of blancoDb
 * 
 * @author Toshiki Iga
 */
public interface BlancoDbQuery /* extends Closeable */{
    /**
     * Set connection object.
     * 
     * @param conn
     */
    void setConnection(final Connection conn);

    /**
     * Get query string
     * 
     * @return SQL string.
     */
    String getQuery();

    /**
     * Prepare JDBC Statement.
     * 
     * @throws SQLException
     *             When SQL Exception occurred.
     */
    void prepareStatement() throws SQLException;

    /**
     * Get JDBC Statement object.
     * 
     * @deprecated Shound NOT user this method.
     */
    PreparedStatement getStatement();

    /**
     * Close JDBC Statement.
     * 
     * @throws IOException
     *             When SQL Exception occurred.
     */
    // void close() throws IOException;
}
