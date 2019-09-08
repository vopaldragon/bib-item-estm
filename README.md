#　fastTextを使った書誌事項の推定

- NDLの国立国会図書館デジタルコレクション書誌情報オープンデータセット（https://www.ndl.go.jp/jp/dlib/standards/opendataset/index.html）からファイルをダウンロードしてdataディレクトリに格納
- MavenでBuildしてCreateDataを実行
- fastText（https://github.com/facebookresearch/fastText）を落としてきてBuild
- ./fasttext supervised -input train.txt -output model
- ./fasttext test model.bin test_xx.txt 1
- ./fasttext predic model.bin test_xx.txt