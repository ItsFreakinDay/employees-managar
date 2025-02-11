package employees.manager.module.ui;

import com.itextpdf.text.Font;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import employees.manager.module.dao.EmployeeDao;
import employees.manager.module.models.Employee;

import javax.swing.*;
import java.awt.Image;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.List;

public class MenuPanel extends JPanel {

    private JTable table;

    public MenuPanel() {
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(240, 240, 240));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = new ImageIcon(ClassLoader.getSystemResource("icons/images.png"));
        ImageIcon scaledIcon = new ImageIcon(logoIcon.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH));
        logoLabel.setIcon(scaledIcon);

        JLabel wishesLabel = new JLabel("Добро пожаловать! Успехов в работе.");

        wishesLabel.setForeground(Color.GRAY);

        headerPanel.add(logoLabel);
        headerPanel.add(wishesLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BorderLayout());
        footerPanel.setBackground(new Color(240, 240, 240));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel versionLabel = new JLabel("Версия: v. 0.0.1");
        versionLabel.setForeground(Color.DARK_GRAY);

        footerPanel.add(versionLabel, BorderLayout.WEST);
        add(footerPanel, BorderLayout.SOUTH);

        // Center Panel with Buttons
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton settingsButton = new JButton("Настройки");
        JButton reportButton = new JButton("Генерация PDF-отчета");
        JButton projectLinkButton = new JButton("Ссылка на проект");
        JButton refreshButton = new JButton("Обновить данные");
        JButton exportButton = new JButton("Экспорт в Excel");
        JButton helpButton = new JButton("Помощь");
        JButton exitButton = new JButton("Выход");



        settingsButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Функция еще не реализована.", "Настройки", JOptionPane.INFORMATION_MESSAGE));

        reportButton.addActionListener(e -> generatePdfReport());

        projectLinkButton.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        refreshButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Данные обновлены.", "Обновление", JOptionPane.INFORMATION_MESSAGE));

        exportButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Экспорт завершен.", "Экспорт", JOptionPane.INFORMATION_MESSAGE));

        helpButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Справка по работе с программой.", "Помощь", JOptionPane.INFORMATION_MESSAGE));

        exitButton.addActionListener(e -> System.exit(0));

        centerPanel.add(settingsButton);
        centerPanel.add(reportButton);
        centerPanel.add(projectLinkButton);
        centerPanel.add(refreshButton);
        centerPanel.add(exportButton);
        centerPanel.add(helpButton);
        centerPanel.add(exitButton);

        add(centerPanel, BorderLayout.CENTER);

        // Загрузка данных и создание таблицы
        loadDataAndCreateTable();
    }

    private void loadDataAndCreateTable() {
        // Получаем список всех сотрудников
        EmployeeDao dao = new EmployeeDao();
        List<Employee> employees = dao.getAllEmployees();

        // Создаем модель таблицы на основе полученных данных
        Object[][] data = new Object[employees.size()][8]; // 8 колонок
        for (int i = 0; i < employees.size(); i++) {
            Employee emp = employees.get(i);
            data[i][0] = emp.getId();
            data[i][1] = emp.getPersonnelNumber();
            data[i][2] = emp.getFullName();
            data[i][3] = emp.getBirthDate();
            data[i][4] = emp.getEmail();
            data[i][5] = emp.getPhone();
            data[i][6] = emp.getEducation();
            data[i][7] = emp.getCurrentPosition();
        }

        // Создаем таблицу и добавляем ее в панель
        table = new JTable(data, new String[] {
                "ID",
                "Табельный номер",
                "ФИО",
                "Дата рождения",
                "Электронная почта",
                "Телефон",
                "Образование",
                "Должность"
        });
//        JScrollPane scrollPane = new JScrollPane(table);
//        add(scrollPane, BorderLayout.CENTER);
    }

    private void generatePdfReport() {
        try {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50); // Устанавливаем размеры страницы

            String desktopDirectory = System.getProperty("user.home") + "/Desktop/";
            String outputFileName = "employee_report.pdf";

            File directory = new File(desktopDirectory);
            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                if (!created) {
                    JOptionPane.showMessageDialog(this, "Не удалось создать директорию на рабочем столе", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            String fullPath = desktopDirectory + outputFileName;
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fullPath));
            document.open();

            // Путь к файлу шрифта Arial
            String fontFilePath = "/fonts/arialmt.ttf";

            // Загружаем шрифт Arial
            BaseFont arialBaseFont = BaseFont.createFont(fontFilePath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(arialBaseFont, 12, Font.NORMAL);

            // Добавляем заголовок отчета
            Paragraph title = new Paragraph("Отчет по сотрудникам", font);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Создаем таблицу
            PdfPTable pdfTable = new PdfPTable(8);
            pdfTable.setWidthPercentage(100);

            // Заполняем строки таблицы данными из JTable
            for (int i = 0; i < table.getRowCount(); i++) {
                for (int j = 0; j < table.getColumnCount(); j++) {
                    Object value = table.getValueAt(i, j);
                    if (value != null) {
                        PdfPCell cell = new PdfPCell(new Phrase(value.toString(), font));
                        cell.setPadding(5);
                        pdfTable.addCell(cell);
                    }
                }
            }

            // Добавляем таблицу в документ
            document.add(pdfTable);

            document.close();
            writer.close();

            JOptionPane.showMessageDialog(this, "PDF отчет успешно сгенерирован!", "Успех", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при генерации PDF: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
