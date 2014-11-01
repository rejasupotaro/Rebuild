package rejasupotaro.rebuild.test.models;

import android.test.InstrumentationTestCase;

import java.util.List;

import rejasupotaro.rebuild.data.models.Link;
import rejasupotaro.rebuild.test.AssetsUtils;

public class LinkTest extends InstrumentationTestCase {

    public void testSourceToLinkList() {
        {
            String source = null;
            assertEquals(0, Link.Parser.toLinkList(source).size());
        }
        {
            String source = "";
            assertEquals(0, Link.Parser.toLinkList(source).size());
        }
        {
            String source = "<li><a href=\"link0\">text0</a></li><li><a href=\"link1\">text1</a></li><li><a href=\"link2\">text2</a></li>";
            List<Link> linkList = Link.Parser.toLinkList(source);
            for (int i = 0; i < linkList.size(); i++) {
                assertEquals("text" + i, linkList.get(i).getUrl() + i);
            }
        }
        {
            String source = AssetsUtils.read(getInstrumentation(), "show_notes.txt");
            List<Link> linkList = Link.Parser.toLinkList(source);
            assertEquals(18, linkList.size());
        }
    }

    public void testSubstringDescription() {
        {
            String source = null;
            assertEquals("", Link.Parser.substringDescription(source));
        }
        {
            String source = "";
            assertEquals("", Link.Parser.substringDescription(source));
        }
        {
            String source = "伊藤直也さん (@naoya_ito)、宮下剛輔さん (@gosukenator) をゲストに迎えて、Immutable Infrastructure, Docker, Packer, Serf などについて話しました。<h3>Show Notes</h3>...";
            assertEquals("<h3>Show Notes</h3>...", Link.Parser.substringDescription(source));
        }
    }

    public void testGetHref() {
        {
            String source = null;
            assertEquals("", Link.Parser.getHref(source));
        }
        {
            String source = "";
            assertEquals("", Link.Parser.getHref(source));
        }
        {
            String source = "<a href=\"http://rejasupota.ro/\">Hello World</a>";
            assertEquals("http://rejasupota.ro/", Link.Parser.getHref(source));
        }
    }

    public void testGetText() {
        {
            String source = null;
            assertEquals("", Link.Parser.getText(source));
        }
        {
            String source = "";
            assertEquals("", Link.Parser.getText(source));
        }
        {
            String source = "<a href=\"http://rejasupota.ro/\">Hello World</a>";
            assertEquals("Hello World", Link.Parser.getText(source));
        }
    }
}
