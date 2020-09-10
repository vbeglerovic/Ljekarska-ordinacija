package ba.unsa.etf.rpr;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.swing.JRViewer;

import javax.swing.*;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class PrintAppointmentReport extends JFrame {
    public void showReport(Connection conn, Integer appointmentId, String doctor, Integer patientId) throws JRException {
      String masterReportSource = getClass().getResource("/reports/medicalReport.jrxml").getFile();
      String subReportSource = getClass().getResource("/reports/patientData.jrxml").getFile();
      JasperReport jasperMasterReport = JasperCompileManager.compileReport(masterReportSource);
      JasperReport jasperSubReport = JasperCompileManager.compileReport(subReportSource);

      Map<String, Object> parameters = new HashMap();
      parameters.put("subreportParameter", jasperSubReport);
      parameters.put("appointment", appointmentId);
      parameters.put("doctor", doctor);
      parameters.put("patient", patientId);

      JasperFillManager.fillReport(jasperMasterReport,  parameters, conn);
      JRViewer viewer = new JRViewer(JasperFillManager.fillReport(jasperMasterReport,  parameters, conn));
      viewer.setOpaque(true);
      viewer.setVisible(true);
      this.add(viewer);
      this.setSize(700, 500);
      this.setVisible(true);
    }
}
