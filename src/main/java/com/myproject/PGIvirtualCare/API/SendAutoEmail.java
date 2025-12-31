package com.myproject.PGIvirtualCare.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.myproject.PGIvirtualCare.Model.Appointment;
import com.myproject.PGIvirtualCare.Model.Users;
import com.myproject.PGIvirtualCare.Model.Users.UserRole;

import jakarta.mail.internet.MimeMessage;

@Service
public class SendAutoEmail {
	
	@Autowired
	private JavaMailSender mailSender;
	
	public void SendConfirmationEmail(Users user) 
	{
		String login = "PatientLogin";
		if(user.getRole().equals(UserRole.DOCTOR))
		{
			login = "DoctorLogin";
		}
		String subject = "PGI VirtualCare Registration Successful - You can Login Now";
		String message = "<!DOCTYPE html>" +
				"<html>" +
				"<head>" +
				"  <meta charset='UTF-8'>" +
				"  <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
				"  <style>" +
				"    body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }" +
				"    .container { max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 8px; border: 1px solid #ddd; box-shadow: 0px 4px 10px gray; }" +
				"    .header { background-color: #13C5DD; padding: 20px; border-radius: 8px 8px 0 0; color: white; text-align: center; }" +
				"    .header h2 { margin: 0; font-size: 20px; }" +
				"    .content { padding: 20px; color: #333333; }" +
				"    .content p { margin: 15px 0; }" +
				"    .details-table { width: 100%; border-collapse: collapse; margin-top: 20px; }" +
				"    .details-table td { padding: 10px; border: 1px solid #ddd; }" +
				"    .details-table td:first-child { font-weight: bold; background-color: #f2f2f2; width: 30%; }" +
				"    .footer { padding: 20px; font-size: 14px; color: #888888; text-align: center; }" +
				"    .btn { background-color: #13C5DD; color: white; padding: 10px 15px; text-decoration: none; border-radius: 5px; display: inline-block; margin-top: 10px; }" +
				"  </style>" +
				"</head>" +
				"<body>" +
				"  <div class='container'>" +
				"    <div class='header'>" +
				"      <h2>PGI Virtual Care - Registration Status</h2>" +
				"    </div>" +
				"    <div class='content'>" +
				"      <p>Dear " + user.getName() + ",</p>" +
				"      <p>We are delighted to inform you that your registration with <strong>PGI Virtual Care</strong> has been <strong>successful</strong>.</p>" +
				"      <p>You can now access your account and avail online healthcare services using the credentials provided below:</p>" +
				"      <table class='details-table'>" +
				"        <tr><td>Login Link:</td><td><a href='http://192.168.0.141:8181/"+login+"'>http://192.168.0.141:8181/"+login+"</a></td></tr>" +
				"        <tr><td>"+user.getRole()+" ID:</td><td>" + user.getEmail() + "</td></tr>" +
				"        <tr><td>Password:</td><td>" + user.getPassword() + "</td></tr>" +
				"      </table>" +
				"      <p style='margin-top: 20px;'>Click below to login to your account:</p>" +
				"      <a class='btn' href='http://192.168.0.141:8181/"+login+"'>Login Now</a>" +
				"      <p>If you need any assistance, please feel free to contact our support team.</p>" +
				"      <p>Thank you for choosing PGI Virtual Care.</p>" +
				"      <p>Regards,<br><strong>Team PGI Virtual Care</strong><br>📞 +91-9876543210</p>" +
				"    </div>" +
				"    <div class='footer'>" +
				"      &copy; 2025 PGI Virtual Care. All rights reserved." +
				"    </div>" +
				"  </div>" +
				"</body>" +
				"</html>";

		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setTo(user.getEmail());
			helper.setSubject(subject);
			helper.setText(message, true);  // enable html content
			mailSender.send(mimeMessage);
						
		} catch (Exception e) {
		 System.err.println("error"+e.getMessage());
			e.printStackTrace();
		}		
	}
	
	
	public void sendAppointmentCancellationEmail(Appointment appointment) {
	    String subject = "PGI Virtual Care - Appointment Cancelled";

	    String message = "<!DOCTYPE html>" +
	            "<html>" +
	            "<head>" +
	            "<meta charset='UTF-8'>" +
	            "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
	            "<style>" +
	            "  body { font-family: Arial, sans-serif; background-color: #f9f9f9; margin: 0; padding: 0; }" +
	            "  .container { max-width: 600px; margin: auto; background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 10px rgba(0,0,0,0.06); }" +
	            "  .header { background-color: #e53935; color: white; padding: 20px; border-radius: 8px 8px 0 0; text-align: center; }" +
	            "  .header h2 { margin: 0; font-size: 22px; }" +
	            "  .content { padding: 20px; color: #444; }" +
	            "  .content p { line-height: 1.6; margin: 15px 0; }" +
	            "  .details-table { width: 100%; margin-top: 20px; border-collapse: collapse; }" +
	            "  .details-table td { padding: 10px; border: 1px solid #ddd; }" +
	            "  .details-table td:first-child { font-weight: bold; background-color: #fbe9e7; width: 35%; }" +
	            "  .footer { text-align: center; padding: 20px; font-size: 14px; color: #999; }" +
	            "  .btn { background-color: #e53935; color: white; padding: 10px 18px; border-radius: 5px; text-decoration: none; display: inline-block; margin-top: 15px; }" +
	            "</style>" +
	            "</head>" +
	            "<body>" +
	            "<div class='container'>" +
	            "  <div class='header'>" +
	            "    <h2>Appointment Cancelled</h2>" +
	            "  </div>" +
	            "  <div class='content'>" +
	            "    <p>Dear " + appointment.getPatient().getName() + ",</p>" +
	            "    <p>We regret to inform you that your scheduled appointment has been <strong>cancelled</strong>.</p>" +
	            "    <p>Below are the appointment details:</p>" +
	            "    <table class='details-table'>" +
	            "      <tr><td>Doctor:</td><td>" + appointment.getDoctor().getName() + "</td></tr>" +
	            "      <tr><td>Appointment Date & Time:</td><td>" + appointment.getDate() +", "+appointment.getTime() + "</td></tr>" +
	            "      <tr><td>Status:</td><td><strong style='color: #e53935;'>Cancelled</strong></td></tr>" +
	            "    </table>" +
	            "    <p>We apologize for any inconvenience caused. You may <strong>reschedule</strong> your appointment using the link below:</p>" +
	            "    <a class='btn' href='http://192.168.0.141:8181/PatientLogin'>Book Another Appointment</a>" +
	            "    <p>If you have any questions or need assistance, please contact our support team.</p>" +
	            "    <p>Thank you for choosing PGI Virtual Care.</p>" +
	            "    <p>Best regards,<br><strong>Team PGI Virtual Care</strong><br>📞 +91-9876543210</p>" +
	            "  </div>" +
	            "  <div class='footer'>" +
	            "    &copy; 2025 PGI Virtual Care. All rights reserved." +
	            "  </div>" +
	            "</div>" +
	            "</body>" +
	            "</html>";

	    try {
	        MimeMessage mimeMessage = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
	        helper.setTo(appointment.getPatient().getEmail());
	        helper.setSubject(subject);
	        helper.setText(message, true);  // HTML content
	        mailSender.send(mimeMessage);
	    } catch (Exception e) {
	        System.err.println("Error sending email: " + e.getMessage());
	        e.printStackTrace();
	    }
	}
	
	public void sendAppointmentApprovalEmail(Appointment appointment) {
	    String subject = "PGI Virtual Care - Appointment Approved";

	    String message = "<!DOCTYPE html>" +
	            "<html>" +
	            "<head>" +
	            "<meta charset='UTF-8'>" +
	            "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
	            "<style>" +
	            "  body { font-family: Arial, sans-serif; background-color: #f5f5f5; margin: 0; padding: 0; }" +
	            "  .container { max-width: 600px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 10px rgba(0,0,0,0.06); }" +
	            "  .header { background-color: #43a047; color: white; padding: 20px; border-radius: 8px 8px 0 0; text-align: center; }" +
	            "  .header h2 { margin: 0; font-size: 18px; }" +
	            "  .content { padding: 20px; color: #333333; }" +
	            "  .content p { line-height: 1.6; margin: 15px 0; }" +
	            "  .details-table { width: 100%; border-collapse: collapse; margin-top: 20px; }" +
	            "  .details-table td { padding: 10px; border: 1px solid #dddddd; }" +
	            "  .details-table td:first-child { background-color: #e8f5e9; font-weight: bold; width: 35%; }" +
	            "  .btn { background-color: #43a047; color: white; padding: 10px 18px; text-decoration: none; border-radius: 5px; display: inline-block; margin-top: 20px; }" +
	            "  .footer { text-align: center; padding: 20px; font-size: 14px; color: #999999; }" +
	            "</style>" +
	            "</head>" +
	            "<body>" +
	            "<div class='container'>" +
	            "  <div class='header'>" +
	            "    <h2>Appointment Confirmed</h2>" +
	            "  </div>" +
	            "  <div class='content'>" +
	            "    <p>Dear " + appointment.getPatient().getName() + ",</p>" +
	            "    <p>We are pleased to inform you that your appointment has been <strong>approved</strong>.</p>" +
	            "    <p>Please find your appointment details below:</p>" +
	            "    <table class='details-table'>" +
	            "      <tr><td>Doctor:</td><td>" + appointment.getDoctor().getName() + "</td></tr>" +
	            "      <tr><td>Appointment Date & Time:</td><td>" + appointment.getDate()+", "+appointment.getTime() + "</td></tr>" +
	            "      <tr><td>Status:</td><td><strong style='color: #43a047;'>Approved</strong></td></tr>" +
	            "    </table>" +
	            "    <p>You are requested to be available 10 minutes before your appointment time. In case of any issue, please reschedule or contact our support team.</p>" +
	            "    <a class='btn' href='http://192.168.0.141:8181'>Go To Link</a>" +
	            "    <p>Thank you for choosing PGI Virtual Care.</p>" +
	            "    <p>Best regards,<br><strong>Team PGI Virtual Care</strong><br>📞 +91-9876543210</p>" +
	            "  </div>" +
	            "  <div class='footer'>" +
	            "    &copy; 2025 PGI Virtual Care. All rights reserved." +
	            "  </div>" +
	            "</div>" +
	            "</body>" +
	            "</html>";

	    try {
	        MimeMessage mimeMessage = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
	        helper.setTo(appointment.getPatient().getEmail());
	        helper.setSubject(subject);
	        helper.setText(message, true);  // HTML content
	        mailSender.send(mimeMessage);
	    } catch (Exception e) {
	        System.err.println("Error sending email: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

}
