package org.sbtitcourses.mdwiki.util;

import com.lowagie.text.pdf.BaseFont;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.sbtitcourses.mdwiki.util.exception.PdfConversionException;
import org.springframework.core.io.InputStreamResource;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class PdfConverter {

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

    private final String font;
    private final int size;

    private PdfConverter(Builder builder) {
        this.font = builder.font;
        this.size = builder.size;
    }

    public InputStreamResource convert(String markdown) {

        String fontFamily = fonts.get(font);
        String style = String.format("font-family: '%s'; font-size: %dpx", fontFamily, size);

        Parser parser = Parser.builder().build();
        Node documentNode = parser.parse(markdown);
        HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();
        String html = htmlRenderer.render(documentNode);

        Document document = Jsoup.parse(html, "UTF-8");
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        document.body().attributes().add("style", style);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();

            ITextFontResolver fontResolver = renderer.getFontResolver();
            fontResolver.addFont(
                    String.format("fonts/%s.%s", font, "ttf"),
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED
            );

            SharedContext sharedContext = renderer.getSharedContext();
            sharedContext.setPrint(true);
            sharedContext.setInteractive(false);

            renderer.setDocumentFromString(document.html());
            renderer.layout();
            renderer.createPDF(outputStream);

            try (InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray())) {
                return new InputStreamResource(inputStream);
            }

        } catch (IOException e) {
            throw new PdfConversionException("Ошибка конвертации документа");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String font;
        private int size;

        private Builder() {
            this.font = "times";
            this.size = 16;
        }

        public Builder font(String font) {
            if (fonts.containsKey(font)) {
                this.font = font;
            }
            return this;
        }

        public Builder size(int size) {
            if (size >= 6 && size <= 66) {
                this.size = size;
            }
            return this;
        }

        public PdfConverter build() {
            return new PdfConverter(this);
        }
    }
}
