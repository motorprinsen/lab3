Feature: Test SVT Play

  Background:
    Given SVT Play is available
    And a user navigates to start page
    And accepts the cookie consent dialog

  Scenario: Title correctness
    When a user visits SVT Play
    Then the title should be "SVT Play"

  Scenario: Logo visibility
    When a user visits SVT Play
    Then the logo should be displayed

  Scenario: Correct main link texts
    When a user visits SVT Play
    Then the main link texts should be "Start", "Program" and "Kanaler"

  Scenario: Availability link with correct link text
    When a user visits SVT Play
    Then the link to the availability page "https://kontakt.svt.se/guide/tillganglighet" should be visible
    And the link text should be "Tillgänglighet i SVT Play"

  Scenario: Correct title on the availability site
    When a user visits SVT Play
    And navigates to the availability page "https://kontakt.svt.se/guide/tillganglighet"
    Then the heading should be "Så arbetar SVT med tillgänglighet"

  Scenario: Correct number of categories on the Programs page
    When a user visits SVT Play
    And navigates to "Program" page
    Then the number of categories displayed should be 17