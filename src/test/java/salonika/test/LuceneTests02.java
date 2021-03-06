package salonika.test;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;
import salonika.demo.IDocIndexService;
import salonika.demo.LuceneService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class LuceneTests02 {

    /**
     * 测试01
     * 基本的文档查询
     */
    @Test
    public void test01() throws IOException, ParseException {
        IDocIndexService lcs = new LuceneService(new SmartChineseAnalyzer());

        String indexName = "luceneIndex1";
        lcs.newIndex(indexName);
        lcs.addDoc(indexName, "文档1的名称", "凡尔纳之后，纽约时报评价最好看的环球冒险小说");
        lcs.addDoc(indexName, "文档2的名称", "这本小说英文版上市一周即空间纽约时报畅销书青少年排行榜冒险类第5名");
        List<Document> docs1 = lcs.searchDoc(indexName, "环球");
        List<Document> docs2 = lcs.searchDoc(indexName, "环游");
        List<Document> docs3 = lcs.searchDoc(indexName, "环球电影");
        List<Document> docs4 = lcs.searchDoc(indexName, "凡尔纳");
        List<Document> docs5 = lcs.searchDoc(indexName, "凡尔赛");
        List<Document> docs6 = lcs.searchDoc(indexName, "环球冒险");
        List<Document> docs7 = lcs.searchDoc(indexName, "最好");
        List<Document> docs8 = lcs.searchDoc(indexName, "最好看");
        List<Document> docs9 = lcs.searchDoc(indexName, "最好看的");
        List<Document> docs10 = lcs.searchDoc(indexName, "好看的");
        List<Document> docs11 = lcs.searchDoc(indexName, "好看");
        List<Document> docs12 = lcs.searchDoc(indexName, "很好看");
        List<Document> docs13 = lcs.searchDoc(indexName, "纽约");
        List<Document> docs14 = lcs.searchDoc(indexName, "纽约上市");
        List<Document> docs15 = lcs.searchDoc(indexName, "1");
        List<Document> docs16 = lcs.searchDoc(indexName, "文档");

        Assert.assertEquals(1, docs1.size());

        lcs.closeAll();
    }

    /**
     * 测试02
     * 添加、修改、删除
     */
    @Test
    public void test02() throws IOException, ParseException {
        IDocIndexService lcs = new LuceneService(new SmartChineseAnalyzer());
        String indexName = "luceneIndex1";
        lcs.newIndex(indexName);
        lcs.saveDoc(indexName, "文档1的名称", "凡尔纳之后，纽约时报评价最好看的环球冒险小说");
        lcs.saveDoc(indexName, "文档2的名称", "这本小说英文版上市一周即空间纽约时报畅销书青少年排行榜冒险类第5名");
        lcs.saveDoc(indexName, "文档2的名称", "这本小说英文版上市一周即空降纽约时报畅销书青少年排行榜冒险类第5名。修改了一下");

        List<Document> docs1 = lcs.allDocs(indexName);
        Assert.assertEquals(docs1.size(), 2);

        // 删除 文档1，本次删除1个文档
        lcs.delDoc(indexName, "文档1的名称");
        List<Document> docs2 = lcs.allDocs(indexName);
        Assert.assertEquals(docs2.size(), 1);

        // 删除 文档2，本次删除1个文档
        lcs.delDoc(indexName, "文档2的名称");
        List<Document> docs3 = lcs.allDocs(indexName);
        Assert.assertEquals(docs3.size(), 0);

        lcs.closeAll();
    }

    /**
     * 测试03
     * 索引恢复
     */
    @Test
    public void test03() throws IOException {
        IDocIndexService lcs = new LuceneService(new SmartChineseAnalyzer());

        String indexName = "luceneIndex2";
        lcs.newIndex(indexName);

        List<Document> docs0 = new ArrayList<>();
        try {
            docs0.addAll(lcs.allDocs(indexName));
            Assert.assertEquals(docs0.size(), 2);
        } catch (IOException e) {
            // 第一次时会报错，在当前实现范围内为正常现象；该测试用例多此运行 测试索引存储的数据能恢复
            Assert.assertEquals(docs0.size(), 0);
        }

        lcs.saveDoc(indexName, "文档1", "内容1内容1内容1内容1");
        lcs.saveDoc(indexName, "文档2", "内容2内容2内容2内容2");

        List<Document> docs = lcs.allDocs(indexName); // 每次查询的都是快照，实际存储在文档发生变化后跟随变化

        Assert.assertEquals(docs.size(), 2);
    }
}
