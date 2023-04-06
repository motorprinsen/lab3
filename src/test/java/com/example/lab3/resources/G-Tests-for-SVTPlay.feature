Feature: Test SVT Play

  Background:
    Given SVT Play is available

  Scenario: SVT Play should show correct title
    When a user visits SVT Play
    Then the title should be "SVT Play"

  Scenario: SVT Play should show its logo
    When a user visits SVT Play
    Then the logo should be displayed

  Scenario: SVT Play should show correct main link texts
    When a user visits SVT Play
    Then the main link texts should be "Start", "Program" and "Kanaler"

  Scenario: SVT Play should show availability link with correct link text
    When a user visits SVT Play
    Then The link to the availability page "https://kontakt.svt.se/guide/tillganglighet" should be visible
    And the link text should be "Tillgänglighet i SVT Play"

  Scenario: SVT Play should show the correct title on the availability site
    When a user visits SVT Play
    And User navigates to the availability page "https://kontakt.svt.se/guide/tillganglighet"
    Then the heading should be "Så arbetar SVT med tillgänglighet"

  Scenario: SVT Play should list the correct number of categories on the Programs page
    When a user visits SVT Play
    And navigates to "Program" page
    Then the number of categories displayed should be 18