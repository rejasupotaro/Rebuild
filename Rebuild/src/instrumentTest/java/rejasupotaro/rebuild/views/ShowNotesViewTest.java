package rejasupotaro.rebuild.views;

import android.test.AndroidTestCase;
import android.widget.TextView;

import java.util.List;

public class ShowNotesViewTest extends AndroidTestCase {

    public void testBuildShowNotes() {
        {
            String source = null;
            assertEquals(0, ShowNotesView.LinkParser.buildShowNotes(getContext(), source).size());
        }
        {
            String source = "";
            assertEquals(0, ShowNotesView.LinkParser.buildShowNotes(getContext(), source).size());
        }
        {
            String source = "<li><a href=\"link0\">text0</a></li><li><a href=\"link1\">text1</a></li><li><a href=\"link2\">text2</a></li>";
            List<TextView> linkTextViewList = ShowNotesView.LinkParser.buildShowNotes(getContext(), source);
            for (int i = 0; i < linkTextViewList.size(); i++) {
                assertEquals("text" + i, linkTextViewList.get(i).getText().toString() + i);
            }
        }
    }

    public void testSubstringDescription() {
        {
            String source = null;
            assertEquals("", ShowNotesView.LinkParser.substringDescription(source));
        }
        {
            String source = "";
            assertEquals("", ShowNotesView.LinkParser.substringDescription(source));
        }
        {
            String source = "伊藤直也さん (@naoya_ito)、宮下剛輔さん (@gosukenator) をゲストに迎えて、Immutable Infrastructure, Docker, Packer, Serf などについて話しました。<h3>Show Notes</h3>...";
            assertEquals("<h3>Show Notes</h3>...", ShowNotesView.LinkParser.substringDescription(source));
        }
    }

    public void testGetHref() {
        {
            String source = null;
            assertEquals("", ShowNotesView.LinkParser.getHref(source));
        }
        {
            String source = "";
            assertEquals("", ShowNotesView.LinkParser.getHref(source));
        }
        {
            String source = "<a href=\"http://rejasupota.ro/\">Hello World</a>";
            assertEquals("http://rejasupota.ro/", ShowNotesView.LinkParser.getHref(source));
        }
    }

    public void testGetText() {
        {
            String source = null;
            assertEquals("", ShowNotesView.LinkParser.getText(source));
        }
        {
            String source = "";
            assertEquals("", ShowNotesView.LinkParser.getText(source));
        }
        {
            String source = "<a href=\"http://rejasupota.ro/\">Hello World</a>";
            assertEquals("Hello World", ShowNotesView.LinkParser.getText(source));
        }
    }
}
