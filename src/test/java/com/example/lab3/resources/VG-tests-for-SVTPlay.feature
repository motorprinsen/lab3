Feature: VG tests for SVT Play

  TODO:
  1. Nyhetsbrev: Enter email address but don't tick the terms/age box
  2. Kanaler: Check that the correct time is shown
  3. Program med kommande program (Bäst i test): Navigate to show, find "Kommande" and verify that more episodes are shown
  4. Program: Verify that only shows starting with A is listed under A (loop through to ö) and that shows with numbers are beneath #
  5. Kanaler: Find a show that should have started and verify the progress bar
  6. Kanaler: Pick a date a bit into the future and verify that "Det finns ingen tablå för valt datum" is displayed
  7. Click on a movie under "Filmtips" and verify that popup is shown
  8. Verify fullscreen in player

  Background:
    Given SVT Play is available

  Scenario: SVT Play should show correct title
    When User visits SVT Play
    Then The title should be "SVT Play"