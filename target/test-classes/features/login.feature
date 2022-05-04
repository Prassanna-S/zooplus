Feature: Login to HRM Application 
  
   @ValidCredentials
   Scenario: Login with valid credentials
      
    Given User is on zooplus page
    Then User accept the cookies in popup
    Then Add 1 item in the recommendations to cart
    Then Check the url contains "overview"
    Then Add 3 item in the recommendations to cart
    Then Sort the prices in descending order
    Then "Increase" the "Lowest" Priced Item
    Then "Delete" the "Highest" Priced Item
    And Check SubTotal and Total are correct
    Then Change the shipping country to "Portugal" and postcode as "5000"
    And Check SubTotal and Total are correct