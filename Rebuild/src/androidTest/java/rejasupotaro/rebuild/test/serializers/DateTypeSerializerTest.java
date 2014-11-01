package rejasupotaro.rebuild.test.serializers;

import android.test.AndroidTestCase;

import java.util.Date;

import rejasupotaro.rebuild.data.serializers.DateTypeSerializer;
import rejasupotaro.rebuild.utils.DateUtils;

public class DateTypeSerializerTest extends AndroidTestCase {

    public void testSerialize() {
        Date pubDate = DateUtils.pubDateToDate("Wed, 08 May 2013 00:00:00 -0700");
        DateTypeSerializer serializer = new DateTypeSerializer();
        Long serializedDate = serializer.serialize(pubDate);
        Date deserializedDate = serializer.deserialize(serializedDate);
        String postedAt = DateUtils.dateToString(deserializedDate);
        assertEquals("May 08 2013", postedAt);
    }
}
