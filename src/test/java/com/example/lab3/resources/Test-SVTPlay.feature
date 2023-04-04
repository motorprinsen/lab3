Feature: Test SVT Play

  Scenario: SVT Play should show correct title
    Given SVT Play is available
    When User visits SVT Play
    Then The title should be "SVT Play"

  Scenario: SVT Play should show its logo
    Given SVT Play is available
    When User visits SVT Plaý
    Then The logo should be displayed

  Scenario: SVT Play should show correct main link texts
    Given SVT Play is available
    When User visits SVT Play
    Then The main link texts should be "Start", "Program" and "Kanaler"

  Scenario: SVT Play should show availability link with correct link text
    Given SVT Play is available
    When User visits SVT Play
    Then The link to the availability site "https://www.svtplay.se/tillganglighet" should be visible
    And The link text should be "Tillgänglighet på SVT Play"

  Scenario: SVT Play should show the correct title on the availability site
    Given SVT Play is available
    When User visits SVT Play
    And User navigates to the availability site
    Then The heading should be "Tillgänglighet på SVT Play"

  Scenario: SVT Play should list the correct number of categories on the Programs page
    Given SVT Plat is available
    When User visits SVT Play
    And User navigates to "Program" page
    Then The number of categories should be 18