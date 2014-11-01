package rejasupotaro.rebuild.test.models;

import android.test.InstrumentationTestCase;

import rejasupotaro.rebuild.data.models.Sponsor;
import rejasupotaro.rebuild.test.AssetsUtils;

public class SponsorTest extends InstrumentationTestCase {

    public void testFromSource() {
        {
            String source = null;
            Sponsor sponsor = Sponsor.fromSource(source);
            assertNotNull(sponsor);
            assertEquals("", sponsor.getText());
            assertEquals("", sponsor.getUrl());
        }
        {
            String source = "";
            Sponsor sponsor = Sponsor.fromSource(source);
            assertNotNull(sponsor);
            assertEquals("", sponsor.getText());
            assertEquals("", sponsor.getUrl());
        }
        {

            String source = "<p>スポンサー: <a href=\"url\">text</a></p>";
            Sponsor sponsor = Sponsor.fromSource(source);
            assertNotNull(sponsor);
            assertEquals("text", sponsor.getText());
            assertEquals("url", sponsor.getUrl());
        }
        {
            String source = AssetsUtils.read(getInstrumentation(), "show_notes.txt");
            Sponsor sponsor = Sponsor.fromSource(source);
            assertNotNull(sponsor);
            assertEquals("Ruby on Rails チュートリアル", sponsor.getText());
            assertEquals("http://railstutorial.jp/", sponsor.getUrl());
        }
    }

}
