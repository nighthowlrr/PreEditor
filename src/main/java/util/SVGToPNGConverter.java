package util;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SVGToPNGConverter {

    public SVGToPNGConverter() {
        this(System.getProperty("user.dir"));
    }

    public SVGToPNGConverter(String startDir) {
        File inputFile;
        File outputFile;

        JFileChooser inputFileChooser = new JFileChooser(new File(startDir));
        inputFileChooser.setFileFilter(new FileNameExtensionFilter("SVG Files (.svg)", "svg"));

        if (inputFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            inputFile = inputFileChooser.getSelectedFile();

            JFileChooser outputFileChooser = new JFileChooser(new File(startDir));
            outputFileChooser.setFileFilter(new FileNameExtensionFilter("PNG Files (.png)", "png"));

            if (outputFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                outputFile = outputFileChooser.getSelectedFile();

                if (! outputFile.getName().substring(outputFile.getName().lastIndexOf(".") + 1)
                        .equalsIgnoreCase("png")) {
                    outputFile = new File(outputFile.getAbsolutePath() + ".png");
                }

                convertSVGtoPNG(inputFile, outputFile);
            }
        }
    }

    private void convertSVGtoPNG(@NotNull File svgFile, @NotNull File pngOutput) {
        File parentFolder = svgFile.getParentFile();
        String svgFileName = svgFile.getName().substring(0, svgFile.getName().lastIndexOf('.'));

        try {
            String svgURL = svgFile.toURI().toURL().toString();
            TranscoderInput inputSVGImage = new TranscoderInput(svgURL);

            OutputStream pngOutStream = new FileOutputStream(pngOutput);
            TranscoderOutput outputPNGImage = new TranscoderOutput(pngOutStream);

            PNGTranscoder pngTranscoder = new PNGTranscoder();

            pngTranscoder.transcode(inputSVGImage, outputPNGImage);

            pngOutStream.flush();
            pngOutStream.close();

        } catch (TranscoderException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
