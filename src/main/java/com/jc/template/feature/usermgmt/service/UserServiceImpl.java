package com.jc.template.feature.usermgmt.service;

import com.jc.template.common.entity.User;
import com.jc.template.feature.usermgmt.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    //    private static final long LOCK_DURATION_TIME_ONE_HOUR = 1*60*60*1000;
    private static final long LOCK_DURATION_TIME_10_SEC = 10 * 1000L;
    public static final long attemptTime = 3;

    public User save(User user, String url) {
        user.setRole("ROLE_USER");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setVerificationCode(UUID.randomUUID().toString());

        user.setAccountNonLocked(true);
        user.setFailedAttempts(0);
        user.setLockTime(null);


        //sendVerificationMail(savedUserInfo, url);
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User checkEmailAndMobile(String email, String mobileNumber) {
        return userRepository.findByEmailAndMobileNumber(email, mobileNumber);
    }

    public boolean verifyAccount(String code) {
        User byVerificationCode = userRepository.findByVerificationCode(code);
        if (byVerificationCode != null) {
            byVerificationCode.setAccountNonLocked(true);
            byVerificationCode.setEnabled(true);
            byVerificationCode.setVerificationCode("");
            userRepository.save(byVerificationCode);
            return true;
        }
        return false;
    }

    public void increaseFailedAttempts(User userInfo) {
        int attempt = userInfo.getFailedAttempts() + 1;
        userRepository.updateByFailedAttempts(attempt, userInfo.getEmail());
    }

    public void resetAttempt(String email) {
        userRepository.updateByFailedAttempts(0, email);
    }

    public void lock(User userInfo) {
        userInfo.setAccountNonLocked(false);
        userInfo.setLockTime(Calendar.getInstance().getTime());
        userRepository.save(userInfo);
    }

    public boolean unlockAccountAsLockTimeExpired(User userInfo) {
        if (userInfo.getLockTime() == null) {
            return false;
        }
        long lockTime = userInfo.getLockTime().getTime();
        long currentTime = Calendar.getInstance().getTime().getTime();
        if (lockTime + LOCK_DURATION_TIME_10_SEC < currentTime) {
            userInfo.setAccountNonLocked(true);
            userInfo.setLockTime(null);
            userInfo.setFailedAttempts(0);
            userRepository.save(userInfo);
            return true;
        }
        return false;
    }

    //@Async
//    public void sendVerificationMail(User user, String url) {
//        String from = "jbytrain@gmail.com";
//        String to = user.getEmail();
//        String subject = "Account verification";
//        String content = "Dear [[name]],<br>"
//                + "Please click the link below to verify your registration:<br>"
//                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a><h3>"
//                + "Thank you,<br>"
//                + "From JC";
//
//        try {
//            MimeMessage mimeMessage = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
//            helper.setFrom(from);
//            helper.setTo(to);
//            helper.setSubject(subject);
//
//            content = content.replace("[[name]]", user.getName());
//            String siteUrl = url + "/verify?code=" + user.getVerificationCode();
//            content = content.replace("[[URL]]", siteUrl);
//
//            helper.setText(content, true);
//            Thread thread = new Thread(new Runnable() {
//                /**
//                 * When an object implementing interface {@code Runnable} is used
//                 * to create a thread, starting the thread causes the object's
//                 * {@code run} method to be called in that separately executing
//                 * thread.
//                 * <p>
//                 * The general contract of the method {@code run} is that it may
//                 * take any action whatsoever.
//                 *
//                 * @see Thread#run()
//                 */
//                @Override
//                public void run() {
//                    mailSender.send(mimeMessage);
//                }
//            });
//            thread.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
