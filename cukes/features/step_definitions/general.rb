Given(/^a user adds a question and answer$/) do
  goto_homepage
  click_link('Create Question')
  @question = 'Why do I smell?'
  @answer = 'Because I didn''t shower'
  fill_in('question', :with => @question)
  fill_in('answer', :with => @answer)
end

def goto_homepage
  visit '/'
end
