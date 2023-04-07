Feature: VG tests for SVT Play

  Background:
    Given SVT Play is available
    And a user navigates to start page
    And accepts the cookie consent dialog

  Scenario: No newsletter registration without email address
    When a user clicks on the newsletter link ("Nyhetsbrev")
    But does not enter an email address
    And clicks the sign-up button ("Prenumerera")
    Then the error message "Du måste ange en giltig e-postaddress!" should show

#  Scenario: No newsletter registration without consent
#    When a user clicks on the newsletter link ("Nyhetsbrev")
#    And enter an email address
#    But does not tick the consent box
#    And clicks the sign-up button ("Prenumerera")
#    Then the error message "Du måste godkänna våra villkor för att kunna prenumerera!" should show
#
#  Scenario: Current programs should reflect the current time
#    When a user clicks on "Kanaler"
#    Then the first listed program should have a start time before the current time
#
#  Scenario: Verify program listings
#    When a user clicks on "Program"
#    Then all programs should be listed correctly and according to the first letter in the program name
#
#  Scenario: Search should return relevant results
#    When a user searches for "bäst i test"
#    Then the first listed program should be "Bäst i test"
#
#  Scenario: Search for random letters should not return any results
#    When a user searches for "qwerty"
#    Then no results should be shown
#    And the text "Inget resultat för sökningen "qwerty"" should be shown
#
#  Scenario: Fullscreen video player
#    When a user clicks on the first available program
#    And starts the video player
#    And clicks the fullscreen button ("Fullskärm")
#    Then the player should be shown fullscreen
#
#  Scenario: Program listed as "Sista chansen" should display an overlay
#    When a user clicks on a program in the "Sista chansen" section
#    Then the program should display an overlay indicating the time left to view it