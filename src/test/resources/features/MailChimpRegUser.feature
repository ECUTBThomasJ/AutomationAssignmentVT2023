Feature: MailChimpRegUser

  Scenario Outline: Register New User
    Given "<browser>" is used to access page
    And a unique "<username>" have been entered
    And a valid "<email>" have been entered
    And a valid "<password>" have been chosen
    When the sign up button is pressed
    Then a new account is "<created or not>" with "<message>"

    Examples: #Usernames are timestamped and will be unique everytime (except in Ex:2,3,6 and 7)
      | browser | username     | email                       | password      | created or not | message                                     |
      | chrome  | LasseKongo   | lasse@kongo.se              | XDR%6tfc      | created        | success                                     |
      | chrome  | #100Chars    | 100chars@e-post.se          | Char100!      | not created    | Enter a value less than 100 characters long |
      | chrome  | #alreadyUsed | seleniumTestingTJ@proton.me | alreadyUsed1# | not created    | Username already used                       |
      | chrome  | LasseKongo   |                             | XDR%6tfc      | not created    | An email address must contain a single @.   |
      | firefox | LasseKongo   | lasse@kongo.se              | XDR%6tfc      | created        | success                                     |
      | firefox | #100Chars    | 100chars@e-post.se          | Char100!      | not created    | Enter a value less than 100 characters long |
      | firefox | #alreadyUsed | seleniumTestingTJ@proton.me | alreadyUsed1# | not created    | Username already used                       |
      | firefox | LasseKongo   |                             | XDR%6tfc      | not created    | An email address must contain a single @.   |

