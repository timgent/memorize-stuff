Given(/^a user adds (\d+) questions and answers$/) do |number_of_questions|
  num_of_qs_int = number_of_questions.to_i

  for x in 1..num_of_qs_int do
    add_question('Q'+x.to_s, 'A'+x.to_s)
  end
end


def add_question(question, answer)
  go_to_homepage
  click_link('Add Questions')
  @question = question
  @answer = answer
  fill_in('question', :with => @question)
  fill_in('answer', :with => @answer)
  click_button('Add this question')
end

Given(/^a user adds (?:a|another) question and answer$/) do
  add_question('My question?', 'My answer')
end


Then(/^the user receives a message confirming that the question has been added$/) do
  expect(page).to have_content 'Question added!'
end