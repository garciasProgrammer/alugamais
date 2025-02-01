package br.com.alugamais.service;

import br.com.alugamais.web.config.hibernate.HibernateConfig;
import br.com.alugamais.web.config.hibernate.TenantContext;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.Connection;
import java.util.Map;

@Service
public class ReportService {

    public byte[] generateReport(InputStream path, Map<String, Object> parameters) throws Exception {

        JasperReport jasperReport = JasperCompileManager.compileReport(path);

        // Identificar o tenant atual
        HibernateConfig config = new HibernateConfig();
        String tenantId = TenantContext.getCurrentTenant();

        // Obter a conexão de banco de dados baseada no tenant
        Connection conexao = config.tenantConnectionProvider().getConnection(tenantId);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexao);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public Connection dataSource() throws Exception {
        // Identificar o tenant atual
        HibernateConfig config = new HibernateConfig();
        String tenantId = TenantContext.getCurrentTenant();

        // Obter a conexão de banco de dados baseada no tenant
        Connection conexao = config.tenantConnectionProvider().getConnection(tenantId);

        return conexao;
    }

    public byte[] generateReportContratos(InputStream path, Map<String, Object> parameters, String reportFormat) throws Exception {
        JasperReport jasperReport = JasperCompileManager.compileReport(path);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Identificar o tenant atual
        HibernateConfig config = new HibernateConfig();
        String tenantId = TenantContext.getCurrentTenant();

        // Obter a conexão de banco de dados baseada no tenant
        Connection conexao = config.tenantConnectionProvider().getConnection(tenantId);


        if ("pdf".equalsIgnoreCase(reportFormat)) {
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexao);
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } else if ("rtf".equalsIgnoreCase(reportFormat)) {
            File tempFile = null;
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conexao);

            JRRtfExporter exporter = new JRRtfExporter();
            // configura as propriedades do exporter
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, new ByteArrayOutputStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);

            // executa a exportação para RTF
            exporter.exportReport();

            byte[] rtfBytes = out.toByteArray(); // obtém os bytes do arquivo RTF em memória
            ByteArrayInputStream in = new ByteArrayInputStream(rtfBytes);
            // grava o conteúdo do stream em um arquivo temporário
            tempFile = File.createTempFile("temp", ".rtf");
            tempFile.deleteOnExit();
            OutputStream outWord = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) > 0) {
                outWord.write(buffer, 0, len);
            }
            outWord.close();
            in.close();
            return rtfBytes;
        } else {
            throw new IllegalArgumentException("Formato de relatório não suportado: " + reportFormat);
        }
    }
}



