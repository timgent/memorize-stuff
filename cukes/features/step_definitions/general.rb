Given(/^a user adds (?:a|another) question and answer$/) do
  add_question('Q1', 'Q2')
end

def goto_homepage
  visit '/'
end


Then(/^the user receives a message confirming that the question has been added$/) do
  expect(page).to have_content 'Question added!'
end

Given(/^a user adds (\d+) questions and answers$/) do |number_of_questions|
  for x in 1..number_of_questions.to_i do
    add_question('Q'+x, 'A'+x)
  end
end





def add_question(question, answer)
  goto_homepage
  click_link('Add Questions')
  @question = question
  @answer = answer
  fill_in('question', :with => @question)
  fill_in('answer', :with => @answer)
  click_button('Add this question')
end