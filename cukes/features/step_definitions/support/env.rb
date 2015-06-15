require 'rspec/expectations'
require 'capybara/cucumber'
require 'capybara/poltergeist'
require 'syck'
require 'faraday_middleware'
require 'gmail'
require 'mime'
require 'mongo'

Capybara.app_host = 'http://localhost:9000'
Capybara.run_server = false # don't start Rack

if ENV['IN_BROWSER']
  # On demand: non-headless tests via Selenium/WebDriver
  # To run the scenarios in browser (default: Firefox), use the following command line:
  # IN_BROWSER=true bundle exec cucumber
  # or (to have a pause of 1 second between each step):
  # IN_BROWSER=true PAUSE=1 bundle exec cucumber
  Capybara.default_driver = :selenium
  AfterStep do
    sleep (ENV['PAUSE'] || 0).to_i
  end
else
  # DEFAULT: headless tests with poltergeist/PhantomJS
  options = {
      :js_errors => false,
      :timeout => 180,
      :window_size => [1280, 1024],
      :debug => false,
      :phantomjs_options => ['--ignore-ssl-errors=true'],
      :inspector => false
  }
  Capybara.register_driver :poltergeist do |app|
    Capybara::Poltergeist::Driver.new(app,options)
  end
  Capybara.default_driver    = :poltergeist
  Capybara.javascript_driver = :poltergeist
  Capybara.automatic_reload
end

mongo_client = Mongo::Client.new('mongodb://127.0.0.1:27017/memorize_stuff')

Before do
  mongo_client[:testResults].find().delete_many
end