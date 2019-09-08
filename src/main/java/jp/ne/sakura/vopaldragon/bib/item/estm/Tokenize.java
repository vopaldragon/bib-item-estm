package jp.ne.sakura.vopaldragon.bib.item.estm;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.codelibs.neologd.ipadic.lucene.analysis.ja.JapaneseTokenizer;
import org.codelibs.neologd.ipadic.lucene.analysis.ja.tokenattributes.PartOfSpeechAttribute;

public class Tokenize {

    public static void main(String[] args) throws Exception {
        for (File file : new File("text").listFiles()) {
            if (!file.getName().endsWith("txt")) continue;
            List<String> lines = new ArrayList<>();
            for (String line : Files.readAllLines(file.toPath(), StandardCharsets.UTF_8)) {
                lines.add(String.join(" ", tokenize(line, file.getName())));
            }
            FileUtils.writeLines(FileUtils.getFile("toknize", file.getName()), "UTF-8", lines);
        }
        words.print();
    }

    static HashCounter<String> words = new HashCounter<>();

    public static List<String> tokenize(String src, String year) {
        List<String> ret = new ArrayList<>();

        try (JapaneseTokenizer jt = new JapaneseTokenizer(null, false, JapaneseTokenizer.Mode.NORMAL)) {
            jt.setReader(new StringReader(src));
            CharTermAttribute ct = jt.addAttribute(CharTermAttribute.class);
            PartOfSpeechAttribute pt = jt.addAttribute(PartOfSpeechAttribute.class);
            jt.reset();
            while (jt.incrementToken()) {
                if (StringUtils.isBlank(ct.toString()) || ct.toString().equals("　")) {
                } else {
                    words.countPlus(ct.toString() + "\t" + pt.getPartOfSpeech() + "\t" + year);
//                    if (pt.getPartOfSpeech().startsWith("名詞") && !pt.getPartOfSpeech().contains("数") && !pt.getPartOfSpeech().contains("非自立")) {
                    ret.add(ct.toString());
//                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

}
