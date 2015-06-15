And(/^then completes a test$/) do
  go_to_test_page
  answer_questions(4, true)
  answer_questions(6, false)
end

Then(/^they will be presented with their test score marked as (.*)$/) do |mark|
  expect(page).to have_content('mark)
end

def answer_questions(number_of_questions, is_correct)
  num_of_qs_int = number_of_questions.to_i

  for x in 1..num_of_qs_int do
    fill_in('Answer', :with => 'this is my final answer!')
    click_link('Show answer')
    if is_correct
      click_button('I\'m right!')
    else
      click_button('I\'m wrong :(')
    end
  end

end

def go_to_test_page
  go_to_homepage
  click_link('Test Me!')
end

