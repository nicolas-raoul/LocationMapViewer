/*
 * Copyright (C) 2015 k3b
 *
 * This file is part of de.k3b.android.LocationMapViewer (https://github.com/k3b/LocationMapViewer/) .
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
package de.k3b.geo.io.gpx;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;

import de.k3b.geo.api.GeoPointDto;
import de.k3b.geo.api.IGeoPointInfo;

/**
 * Created by k3b on 14.06.2014.
 */
public class GpxReaderTest {
    String xmlMinimal_gpx_v11 = "<trkpt lat='53.1099972' lon='8.7178206'><time>2014-12-19T21:13:21Z</time><name>262:3:562:54989</name><desc>type: cell, accuracy: 1640, confidence: 75</desc><link>geo:0,0?q=12.34,56.78(name)</link></trkpt>\n";

    String xmlFull_gpx_v11 = "<gpx xmlns='http://www.topografix.com/GPX/1/1' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' version='1.1' xsi:schemaLocation='http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd' creator='Location Cache Map'>"+
            "<trk><trkseg>" + xmlMinimal_gpx_v11 + "</trkseg></trk></gpx>";

    String xmlMinimal_kml = "<Placemark><name>262:3:562:54989</name><description>type: cell, accuracy: 1640, confidence: 75</description><Point><coordinates>8.7178206,53.1099972,0</coordinates></Point><when>2014-12-19T21:13:21Z</when></Placemark>\n";

    @Test
    public void parseFormatGpx11ShortTest() throws IOException {
        GpxReader reader = new GpxReader(null);
        IGeoPointInfo location = reader.getTracks(new InputSource(new StringReader(xmlMinimal_gpx_v11))).get(0);
        String formatted = GpxFormatter.toGpx(new StringBuffer(), location, location.getDescription(), location.getUri()).toString();

        Assert.assertEquals(xmlMinimal_gpx_v11, formatted);
    }

    String xmlMinimal_gpx_v10 = "<wpt lat='53.1099972' lon='8.7178206'><time>2014-12-19T21:13:21Z</time><name>262:3:562:54989</name><desc>type: cell, accuracy: 1640, confidence: 75</desc><url>geo:0,0?q=12.34,56.78(name)</url></wpt>\n";

    @Test
    public void parseFormatGpx11FullTest() throws IOException {
        GpxReader reader = new GpxReader(null);
        IGeoPointInfo location = reader.getTracks(new InputSource(new StringReader(xmlFull_gpx_v11))).get(0);
        String formatted = GpxFormatter.toGpx(new StringBuffer(), location, location.getDescription(), location.getUri()).toString();

        Assert.assertEquals(xmlMinimal_gpx_v11, formatted);
    }

    @Test
    public void parseFormatGpx10ShortTest() throws IOException {
        GpxReader reader = new GpxReader(null);
        IGeoPointInfo location = reader.getTracks(new InputSource(new StringReader(xmlMinimal_gpx_v10))).get(0);
        String formatted = GpxFormatter.toGpx(new StringBuffer(), location, location.getDescription(), location.getUri()).toString();

        Assert.assertEquals(xmlMinimal_gpx_v11, formatted);
    }


    @Test
    public void parseFormatKmlShortTest() throws IOException {
        GpxReader reader = new GpxReader(new GeoPointDto());
        GeoPointDto location = (GeoPointDto) reader.getTracks(new InputSource(new StringReader(xmlMinimal_kml))).get(0);

        // uri not supported by kml
        location.setUri("geo:0,0?q=12.34,56.78(name)");

        String formatted = GpxFormatter.toGpx(new StringBuffer(), location, location.getDescription(), location.getUri()).toString();

        Assert.assertEquals(xmlMinimal_gpx_v11, formatted);
    }
}