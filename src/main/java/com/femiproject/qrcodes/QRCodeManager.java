package com.femiproject.qrcodes;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeManager {
    private static final String QR_CODE_FILE = "qr_code.png";
    private static BufferedReader br;

    public static void main(String[] args) throws WriterException {
        InputStreamReader reader = new InputStreamReader(System.in);
        br = new BufferedReader(reader);

        int choice = 0;

        while (choice != 4) {
            displayMenu();

            try {
                choice = Integer.parseInt(br.readLine());

                switch (choice) {
                    case 1 -> generateQRCode();
                    case 2 -> viewQRCode();
                    case 3 -> extractInformation();
                    case 4 -> System.out.println("Exiting... Thank you for using QR Code Manager!");
                    default -> System.out.println("Invalid option! Please choose between 1-4.");
                }
                if (choice != 4) {
                    System.out.println("\nPress Enter to continue...");
                    br.readLine();
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            } catch (IOException e) {
                System.out.println("I/O Error: " + e.getMessage());
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           QR CODE MANAGER");
        System.out.println("=".repeat(50));
        System.out.println("1. Generate QR Code");
        System.out.println("2. View QR Code");
        System.out.println("3. Extract information from QR Code");
        System.out.println("4. Exit");
        System.out.println("-".repeat(50));
        System.out.print("Enter an option: ");
        System.out.println("\n" + "-".repeat(50));
    }

    private static void generateQRCode() throws WriterException {
        try {
            System.out.println("\nGENERATE QR CODE");
            System.out.println("-".repeat(20));
            System.out.println("Enter some text and terminate with blank line...");

            StringBuilder text = new StringBuilder();
            String line = br.readLine();

            while (!line.trim().isEmpty()) {
                text.append(line).append("\n");
                line = br.readLine();
            }

            String textString = text.toString().trim();

            if (textString.isEmpty()) {
                System.out.println("No text entered. QR Code not generated.");
                return;
            }

            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(textString, BarcodeFormat.QR_CODE, 300, 300);
            Path path = Paths.get(QR_CODE_FILE);
            MatrixToImageWriter.writeToPath(matrix, "png", path);

            System.out.println("✓ QR Code successfully generated and saved as: " + QR_CODE_FILE);
            System.out.println("Text encoded: " + textString);

        } catch (IOException e) {
            System.out.println("I/O Error: " + e.getMessage());
        }
    }

    private static void viewQRCode() {
        System.out.println("\nVIEW QR CODE");
        System.out.println("-".repeat(15));

        File qrFile = new File(QR_CODE_FILE);

        if (!qrFile.exists()) {
            System.out.println("❌ No QR Code found! Please generate a QR Code first.");
            return;
        }

        try {
            long fileSize = Files.size(Paths.get(QR_CODE_FILE));

            System.out.println("✓ QR Code file found: " + QR_CODE_FILE);
            System.out.println("File size: " + fileSize + " bytes");
            System.out.println("Location: " + qrFile.getAbsolutePath());

            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.OPEN)) {
                    desktop.open(qrFile);
                    System.out.println("✓ Opening QR Code with default image viewer...");
                } else {
                    System.out.println(
                            "⚠ Cannot open file automatically. Please open " + QR_CODE_FILE + " manually.");
                }
            } else {
                System.out
                        .println("⚠ Desktop operations not supported. Please open " + QR_CODE_FILE + " manually.");
            }
        } catch (IOException e) {
            System.out.println("Error accessing QR Code file: " + e.getMessage());
        }
    }

    private static void extractInformation() {
        System.out.println("\nEXTRACT INFORMATION FROM QR CODE");
        System.out.println("-".repeat(35));

        File qrFile = new File(QR_CODE_FILE);

        if (!qrFile.exists()) {
            System.out.println("❌ No QR Code found! Please generate a QR Code first.");
            return;
        }

        try {
            BufferedImage image = ImageIO.read(qrFile);
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Reader reader = new MultiFormatReader();
            Result result = reader.decode(bitmap);

            System.out.println("✓ QR Code successfully decoded!");
            System.out.println("The QR Code contains the following information:");
            System.out.println("-".repeat(45));
            System.out.println(result.getText());
            System.out.println("-".repeat(45));
            System.out.println("Format: " + result.getBarcodeFormat());

        } catch (IOException e) {
            System.out.println("I/O Error: " + e.getMessage());
        } catch (NotFoundException e) {
            System.out.println("❌ No QR Code found in the image!");
        } catch (ChecksumException | FormatException e) {
            System.out.println("❌ Error reading QR Code: " + e.getMessage());
        }
    }
}
