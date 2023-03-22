Feature: MailChimpRegUser

  Scenario Outline: Register New User
    Given "<browser>" is used to access page
    And a unique "<username>" have been entered
    And a valid "<email>" have been entered
    And a valid "<password>" have been chosen
    When the sign up button is pressed
    Then a new account is "<created>"

    Examples: #Usernames will be unique everytime, since they are timestamped
      | browser | username   | email          | password | created |
      | chrome  | LasseKongo | hej@hej.com    | XDR%6tfc | no      |
      | firefox | LasseKongo | lasse@kongo.se | t6       | no      |