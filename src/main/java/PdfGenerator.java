/**
 * Created by shichen on 2017/8/16.
 */

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import javax.print.Doc;

//入门教程：http://www.cnblogs.com/h--d/p/6150320.html

public class PdfGenerator {

    public static void main(String[] args) throws Exception {

        // 1.新建document对象
        Document document = new Document();

        // 2.建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中。
        // 创建 PdfWriter 对象 第一个参数是对文档对象的引用，第二个参数是文件的实际名称，在该名称中还会给出其输出路径。
        PdfWriter writer = PdfWriter.getInstance(document,
                new FileOutputStream("D:/itext-test.pdf"));

        // 3.打开文档
        document.open();

        // 4.添加一个内容段落，当英文和中文一起显示时，这种字体不好看
        BaseFont baseFont1 = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);

        //方法一：使用Windows系统字体(TrueType)
        //msyh是微软雅黑，msyh.ttc、msyhl.ttc和msyhbd.ttc，分别为标准的，细体的和粗体的
        //https://max.book118.com/html/2017/0111/82461508.shtm
        BaseFont baseFont2 = BaseFont.createFont("C:/Windows/Fonts/msyh.ttc,1", BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);

        //方法二：使用iTextAsian.jar中的字体(此为乱码)
        //BaseFont baseFont3 = BaseFont.createFont("STSong-Light", BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);

        //方法三：使用资源字体(ClassPath)
        //BaseFont baseFont4 = BaseFont.createFont("/couri.TTF",BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);

        Font blueFont = new Font(baseFont2);
        blueFont.setColor(BaseColor.BLUE);
        document.add(new Paragraph("hello,world我要 测试a",blueFont));

        writeProperty(document);
        writeList(document);
        writeImage(document);
        writeFormattedContent(document);

        // 5.关闭文档
        document.close();
        writer.close();
    }

    public static void writeFormattedContent(Document document) throws Exception{
        //中文字体,解决中文不能显示问题
        BaseFont bfChinese = BaseFont.createFont("STSong-Light",
                "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);

        //蓝色字体
        Font blueFont = new Font(bfChinese);
        blueFont.setColor(BaseColor.BLUE);
        //段落文本
        Paragraph paragraphBlue = new Paragraph("paragraphOne blue front", blueFont);
        document.add(paragraphBlue);

        //绿色字体
        Font greenFont = new Font(bfChinese);
        greenFont.setColor(BaseColor.GREEN);
        //创建章节
        Paragraph chapterTitle = new Paragraph("段落标题xxxx", greenFont);
        Chapter chapter1 = new Chapter(chapterTitle, 1);
        chapter1.setNumberDepth(0);

        Paragraph sectionTitle = new Paragraph("部分标题", greenFont);
        Section section1 = chapter1.addSection(sectionTitle);

        Paragraph sectionContent = new Paragraph("部分内容", blueFont);
        section1.add(sectionContent);

        //将章节添加到文章中
        document.add(chapter1);
    }

    public static void writeList(Document document) throws Exception {
        //添加有序列表
        List orderedList = new List(List.ORDERED);
        orderedList.add(new ListItem("Item one"));
        orderedList.add(new ListItem("Item two"));
        orderedList.add(new ListItem("Item three"));
        document.add(orderedList);
    }

    public static void writeImage(Document document) throws Exception {
        //图片1
        Image image1 = Image.getInstance("D:/tmp/clipboard.png");
        //设置图片位置的x轴和y周
        image1.setAbsolutePosition(100f, 550f);
        //设置图片的宽度和高度
        image1.scaleAbsolute(126, 114);
        //将图片1添加到pdf文件中
        document.add(image1);

        //图片2
        Image image2 = Image.getInstance(new URL("http://static.cnblogs.com/images/adminlogo.gif"));
        //将图片2添加到pdf文件中
        document.add(image2);
    }

    public static void writeProperty(Document document) {
        //设置属性
        //标题
        document.addTitle("this is a title");
        //作者
        document.addAuthor("cs");
        //主题
        document.addSubject("this is subject");
        //关键字
        document.addKeywords("Keywords");
        //创建时间
        document.addCreationDate();
        //应用程序
        document.addCreator("hd.com");
    }

    public static void readPdf() throws Exception{
        //读取pdf文件
        PdfReader pdfReader = new PdfReader("C:/Users/H__D/Desktop/test1.pdf");

        //修改器
        PdfStamper pdfStamper = new PdfStamper(pdfReader,
                new FileOutputStream("C:/Users/H__D/Desktop/test10.pdf"));

        Image image = Image.getInstance("C:/Users/H__D/Desktop/IMG_0109.JPG");
        image.scaleAbsolute(50, 50);
        image.setAbsolutePosition(0, 700);

        for(int i=1; i<= pdfReader.getNumberOfPages(); i++)
        {
            PdfContentByte content = pdfStamper.getUnderContent(i);
            content.addImage(image);
        }

        pdfStamper.close();
    }
}