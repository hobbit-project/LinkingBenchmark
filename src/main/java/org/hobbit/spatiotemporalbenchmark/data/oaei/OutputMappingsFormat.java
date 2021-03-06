package org.hobbit.spatiotemporalbenchmark.data.oaei;

/**
 * *****************************************************************************
 * Copyright 2012 by the Department of Computer Science (University of Oxford)
 *
 * This file is part of LogMap.
 *
 * LogMap is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * LogMap is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LogMap. If not, see <http://www.gnu.org/licenses/>.
 *****************************************************************************
 */
public abstract class OutputMappingsFormat {

    protected String output_file;

    protected OutputMappingsFormat(String output_file_str) {

        output_file = output_file_str;

    }

    protected abstract void setOutput() throws Exception;

    protected abstract void saveOutputFile() throws Exception;

    /**
     * Rounded and shifted to [0-1] interval
     *
     * @param conf
     * @return
     */
    protected double getRoundConfidence(double conf) {
        return (double) Math.round(conf * 100.0) / 100.0; //Only two decimals
    }

}
