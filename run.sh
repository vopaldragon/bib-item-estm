fastText-0.9.1/fasttext supervised -input train.txt -output model
echo title
fastText-0.9.1/fasttext test model.bin test_title.txt 1
echo volume
fastText-0.9.1/fasttext test model.bin test_volume.txt 1
echo series
fastText-0.9.1/fasttext test model.bin test_series.txt 1
echo edition
fastText-0.9.1/fasttext test model.bin test_edition.txt 1
echo author
fastText-0.9.1/fasttext test model.bin test_author.txt 1
echo publisher
fastText-0.9.1/fasttext test model.bin test_publisher.txt 1
echo year
fastText-0.9.1/fasttext test model.bin test_year.txt 1
fastText-0.9.1/fasttext predict-prob model.bin predict_title.txt > result_title.txt
fastText-0.9.1/fasttext predict-prob model.bin predict_volume.txt > result_volume.txt
fastText-0.9.1/fasttext predict-prob model.bin predict_series.txt > result_series.txt
fastText-0.9.1/fasttext predict-prob model.bin predict_edition.txt > result_edition.txt
fastText-0.9.1/fasttext predict-prob model.bin predict_author.txt > result_author.txt
fastText-0.9.1/fasttext predict-prob model.bin predict_publisher.txt > result_publisher.txt
fastText-0.9.1/fasttext predict-prob model.bin predict_year.txt > result_year.txt