Feature: Launch Activities

  Scenario: Launch MainActivity
    Then I see "website"
    Then I see "timeline"
    Then I wait
    Then the "Main" activity should be open
    Then I take a picture

  Scenario: Launch SettingsActivity
    When I try to open SettingsActivity
    Then I wait
    Then the "Settings" activity should be open
    Then I take a picture

  Scenario: Launch AboutActivity
    When I try to open AboutActivity
    Then I wait
    Then the "About" activity should be open
    Then I take a picture

  Scenario: Launch LicensesActivity
    When I try to open LicensesActivity
    Then I wait
    Then the "Licenses" activity should be open
    Then I take a picture

  Scenario: Launch TwitterWidgetActivity
    When I try to open TwitterWidgetActivity
    Then I wait
    Then the "TwitterWidget" activity should be open
    Then I take a picture

  Scenario: Launch EpisodeDetailActivity
    When I try to open EpisodeDetailActivity
    Then I wait for 5 seconds
    Then the "EpisodeDetail" activity should be open
    Then I take a picture
