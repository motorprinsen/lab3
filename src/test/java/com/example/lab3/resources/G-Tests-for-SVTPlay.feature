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
    Then the link to the availability page should be visible
    And the link text should be "Tillgänglighet i SVT Play"

  Scenario: Correct title on the availability site
    When a user visits SVT Play
    And navigates to the availability page
    Then the heading should be "Så arbetar SVT med tillgänglighet"

  Scenario: Correct number of categories on the Programs page
    When a user visits SVT Play
    And navigates to "Program" page
    Then the number of categories displayed should be 17

  Scenario: Cookie settings are respected
    When a user visits SVT Play
    And changes the "ad_storage" cookie consent setting
    Then the "ad_storage" setting should be changed

  Scenario: Child protection settings are respected
    When a user visits SVT Play
    And sets a child protection pin code
    Then age restricted programs do not play

  Scenario: Autoplay settings are respected
    When a user visits SVT Play
    And disables autoplay
    Then autoplay should be disabled

  Scenario: Verify visual aid
    When a user visits SVT Play
    And selects a program with visual aid
    Then a link with the text "utan tolkning" should be shown

  Scenario: Searching for empty text should yield no results
    When a user visits SVT Play
    And searches for without entering a search term
    Then the text "Inga sökträffar." should be shown
    