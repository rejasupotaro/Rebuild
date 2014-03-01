require 'calabash-android/calabash_steps'

When /^I try to open SettingsActivity$/ do
  step %{I press the menu key}
  step %{I press the enter button}
end

When /^I try to open AboutActivity$/ do
  step %{I try to open SettingsActivity}
  step %{I press "About"}
end

When /^I try to open LicensesActivity$/ do
  step %{I try to open SettingsActivity}
  step %{I press "OSS Licenses"}
end

When /^I try to open TimelineActivity$/ do
  step %{I touch the "timeline" text}
end

When /^I try to open EpisodeDetailActivity$/ do
  step %{I press the enter button}
end
