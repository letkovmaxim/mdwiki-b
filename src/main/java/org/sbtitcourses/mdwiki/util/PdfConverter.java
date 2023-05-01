package org.sbtitcourses.mdwiki.util;

import com.lowagie.text.pdf.BaseFont;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sbtitcourses.mdwiki.util.exception.PdfConversionException;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Вспомогательный класс, содержащий методы
 * для конвертации markdown-документа в формат PDF.
 */
public final class PdfConverter {

    /**
     * Доступные шрифты.
     */
    private static final Map<String, String> fonts = new HashMap<>() {{
        put("arial", "Arial Unicode MS");
        put("liberation", "Liberation Sans");
        put("truetypewriter", "Truetypewriter PolyglOTT");
        put("rubik", "Rubik");
        put("anonymous", "Anonymous Pro");
        put("calibri", "Calibri");
        put("segoeui", "Segoe UI");
        put("times", "Times New Roman");
    }};

    /**
     * Метод, конвертирующий текст markdown-документа в PDF формат.
     *
     * @param markdown текст markdown-документа.
     * @param font     шрифт.
     * @param size     размер шрифта.
     * @return поток ввода с текстом документа в виде ресурса.
     * @throws PdfConversionException если произошла ошибка при конвертации.
     */
    public static byte[] convert(String markdown, String font, int size, String username) {
        if (size < 6 || size > 66) {
            size = 16;
        }

        String fontFamily = fonts.getOrDefault(font, "Times New Roman");
        String style = String.format("font-family: '%s'; font-size: %dpx", fontFamily, size);

        Parser parser = Parser.builder().build();
        Node documentNode = parser.parse(markdown);
        HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();
        String html = htmlRenderer.render(documentNode);

        Document document = Jsoup.parse(html, "UTF-8");
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        document.body().attributes().add("style", style);

        Elements links = document.select("img[src]");
        for (Element link : links) {
            String linkText = link.attr("src");
            if (linkText.startsWith("http://localhost")) {
                String newLink = "uploads/" + username + linkText.substring(linkText.lastIndexOf("/")) + ".jpg";
                link.attr("src", newLink);
            }
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();

            ITextFontResolver fontResolver = renderer.getFontResolver();
            fontResolver.addFont(
                    String.format("fonts/%s.%s", font, "ttf"),
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED
            );

            renderer.setDocumentFromString(document.html(), null);
            renderer.layout();
            renderer.createPDF(outputStream);

            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new PdfConversionException("Ошибка конвертации документа");
        }
    }
}