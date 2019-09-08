package jp.ne.sakura.vopaldragon.bib.item.estm;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.math.NumberUtils;

public class AnalyzeData {
//[URL, タイトル, 巻次, シリーズ, 版表示, 著者, 出版者, 出版年, ISBN, 冊数（ページ数・大きさ）, NDC分類, NDC分類（第８版）, NDC分類（第９版）, NDLC分類, 件名（NDLSH）, 公開範囲]

    public static void main(String[] args) throws IOException {
        Map<String, Integer> map = new HashMap<>();
        map.put("title", 1);
        map.put("volume", 2);
        map.put("series", 3);
        map.put("edition", 4);
        map.put("author", 5);
        map.put("publisher", 6);
        map.put("year", 7);
        try (RWUtils.Writer anz = RWUtils.writer(Paths.get("anz.txt"))) {
            for (String key : map.keySet()) {
                double okAve = 0;
                int ok = 0;
                double ngAve = 0;
                int ng = 0;
                List<String> inputs = Files.readAllLines(Paths.get("predict_" + key + ".txt"), StandardCharsets.UTF_8);
                List<String> results = Files.readAllLines(Paths.get("result_" + key + ".txt"), StandardCharsets.UTF_8);
                for (int i = 0; i < inputs.size(); i++) {
                    String input = inputs.get(i);
                    String result[] = results.get(i).split(" ");
                    if (result[0].contains(key)) {
                        //OK
                        okAve += NumberUtils.toDouble(result[1]);
                        ok++;
                    } else {
                        anz.writeDataV(key, input, result[0], result[1]);
                        ngAve += NumberUtils.toDouble(result[1]);
                        ng++;
                    }
                }
                System.out.println(key + "\tok\t" + ok + "\t" + (okAve / ok) + "\tng\t" + ng + "\t" + (ngAve / ng));
            }
        }
    }
}
