Feature: Launch Activities

  Scenario: Launch MainActivity
    Then I see "website"
    Then I see "timeline"
    Then I wait
    Then the "Main" activity should be open

  Scenario: Launch SettingsActivity
    Then I press the menu key
    Then I press the enter button
    Then I wait
    Then the "Settings" activity should be open

  Scenario: Launch AboutActivity
    Then I press the menu key
    Then I press the enter button
    Then I press "About"
    Then I wait
    Then the "About" activity should be open

  Scenario: Launch LicensesActivity
    Then I press the menu key
    Then I press the enter button
    Then I press "OSS Licenses"
    Then I wait
    Then the "Licenses" activity should be open

  Scenario: Launch TwitterWidgetActivity
    Then I touch the "timeline" text
    Then I wait
    Then the "TwitterWidget" activity should be open

  Scenario: Launch EpisodeDetailActivity
    Then I press the enter button
    Then I wait
    Then the "EpisodeDetail" activity should be open
