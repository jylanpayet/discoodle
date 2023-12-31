package com.discoodle.api.service;

import com.discoodle.api.model.User;

import com.discoodle.api.request.RegistrationRequest;
import com.discoodle.api.security.mailConfirmation.MailSender;
import com.discoodle.api.security.token.ConfirmationToken;
import com.discoodle.api.security.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

   private final UserService userService;
   private final ConfirmationTokenService confirmationTokenService;
   private final MailSender mailSender;

   public String register(RegistrationRequest request) {
      // Check if username or/and mail entered exist already.
      if (userService.getUserByUserName(request.getUsername()).isEmpty() || userService.getUserByMail(request.getMail()).isEmpty()) {
         // Regex for password with at least 8 characters constitued from at least 1 number, 1 letter to lowercase, 1 letter to uppercase, 1 special character.
         if (request.getPassword().matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}")) {
            // Regex for mail with at (@).
            if (request.getMail().matches("^(.+)@(.+)$")) {
               // Create a token with user's inputs and add user in database.
               String token = userService.signUpUser(
                     new User(
                           request.getMail(),
                           request.getUsername(),
                           request.getPassword(),
                           request.getName(),
                           request.getLast_name(),
                           User.Role.STUDENT
                     )
               );
               // Send mail verifcation.
                    String link = "http://localhost:8080/api/registration/confirm?token=" + token;
                    mailSender.send(request.getMail(),
                            buildMail(request.getName(), link));
               return token;
            }
            return "Votre mail n'est pas valide.\n";
         }
      }

      return """
            Votre mot de passe doit contenir :
            - au moins 8 caractères
            - un chiffre
            - une minuscule
            - une majuscule
            - un caractère spécial
            - pas d'espace, retour à la ligne, etc
            """;
   }

   public String login(RegistrationRequest request) {
      if (userService.getUserByUserName(request.getUsername()).get().isEnabled()) {
         return userService.login(request.getUsername(), request.getPassword());
      }
      return "Votre compte n'est pas activé ou il n'existe pas.";
   }

   @Transactional
   public String confirmToken(String token, HttpServletResponse response) throws IOException {
      // Try to find token if it exists in database.
      ConfirmationToken confirmationToken = confirmationTokenService
            .getToken(token)
            .orElseThrow(() ->
                  new IllegalStateException("Le token n'a pas été trouvé."));

      // Check if token has been already confirmed.
      if (confirmationToken.getConfirmedAt() != null) {
         throw new IllegalStateException("L'adresse mail a déjà été confirmée.");
      }

      LocalDateTime expiredAt = confirmationToken.getExpiresAt();

      // Control if token hasn't expired.
      if (expiredAt.isBefore(LocalDateTime.now())) {
         throw new IllegalStateException("Le token a expiré.");
      }

      // Refresh token's informations with time activation.
      confirmationTokenService.setConfirmedAt(token);
      // Activate account.
      userService.enableUser(
            confirmationToken.getUser().getMail());
      // Redirect user on login page.
      response.sendRedirect("http://localhost:8081/compte");
      return "Vous pouvez désormais vous connecter à notre plateforme discoodle ! :)";
   }

   private String buildMail(String name, String link) {
      // It's mail pattern.
      return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
            "\n" +
            "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
            "\n" +
            "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
            "    <tbody><tr>\n" +
            "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
            "        \n" +
            "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
            "          <tbody><tr>\n" +
            "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
            "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
            "                  <tbody><tr>\n" +
            "                    <td style=\"padding-left:10px\">\n" +
            "                  \n" +
            "                    </td>\n" +
            "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
            "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Discoodle | Confirmez votre mail !</span>\n" +
            "                    </td>\n" +
            "                  </tr>\n" +
            "                </tbody></table>\n" +
            "              </a>\n" +
            "            </td>\n" +
            "          </tr>\n" +
            "        </tbody></table>\n" +
            "        \n" +
            "      </td>\n" +
            "    </tr>\n" +
            "  </tbody></table>\n" +
            "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
            "    <tbody><tr>\n" +
            "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
            "      <td>\n" +
            "        \n" +
            "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
            "                  <tbody><tr>\n" +
            "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
            "                  </tr>\n" +
            "                </tbody></table>\n" +
            "        \n" +
            "      </td>\n" +
            "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
            "    </tr>\n" +
            "  </tbody></table>\n" +
            "\n" +
            "\n" +
            "\n" +
            "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
            "    <tbody><tr>\n" +
            "      <td height=\"30\"><br></td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
            "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
            "        \n" +
            "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hey " + name + " !</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Merci de t'être enregistré(e) sur Discoodle. Clique sur le lien suivant afin de valider ton compte : </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activer maintenant !</a> </p></blockquote>\n Le lien expirera dans 15 minutes. <p>Rejoins-nous vite sur Discoodle ;)</p>" +
            "        \n" +
            "      </td>\n" +
            "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "      <td height=\"30\"><br></td>\n" +
            "    </tr>\n" +
            "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
            "\n" +
            "</div></div>";
   }

}
