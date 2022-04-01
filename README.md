# PostIt

## What will this application do?

My application will be a forum similar to **Reddit**, made up of communities 
that each cater to a different topic or interest. Users can view posts and 
search for a specific communities.

When a new user joins, they can create an account and log in, although this 
is not necessary to view posts. Logging in allows them to:

- post content to the different communities
- like or dislike posts
- comment under posts
- create new communities

Currently, users can only post text-based posts, but in the future, the 
ability to post images will be added.

A logged in user can choose what content they see in their feed by 
subscribing to specific communities. Alternatively, they can visit a specific 
community and view posts that are all related to that commmunity.

Once logged in, a user can also view their profile and write a short bio.

## Who will use it?

This project is mainly aimed at people who spend time online and wish to see 
content that is tailor made to their interests. Due to the wide variety and 
number of communities possible, anyone can find something that interests them. 

## Why is this project of interest to you?

This project is of interest to me as I spend a lot of time on Reddit myself, 
and so I thought it would be interesting to see how the site is built and 
how all the different communities and users are managed. Also, I believe 
that this project is challenging enough to showcase my skills and help me 
get better at building software. I also think this project will translate 
well to the next phases of the project, as its structure allows for the 
easy addition of new features. 

## User Stories

- As a user, I want to be able to upload text posts to a community
- As a user, I want to be able to upload images to a community
- As a user, I want to be able to subscribe to communities and add that community to my list of communities to see the content I want
- As a user, I want to be able to customize my profile with a bio
- As a user, I want to be able to like and comment under posts
- As a user, I want to be able to create and add my own communities to PostIt based on my interests
- As a user, I want to accurately keep track of all the posts I've liked and disliked 
- As a user, I want to be able to view other user's profiles and bios
- As a user, I want to be able to save the posts I create to the forum and access them again
- As a user, I want to be able to save my logged in state and still be logged in when I close the forum and open it again
- As a user, I want to be able to visit communities I create when I reopen the forum
- As a user, I want to be able to have the option to either load the forum from file or create a new, empty one
- As a user, I want to be able to be given the option to save my posts when closing the forum

## Phase 4: Task 2

Mon Mar 28 22:33:15 PDT 2022
Added text post with title "I love gaming" to the gaming community.
Mon Mar 28 22:33:33 PDT 2022
Added the minecraft community to the forum.
Mon Mar 28 22:34:02 PDT 2022
Added image post with title "This is a laptop also" to the fortnite community.
Mon Mar 28 22:34:12 PDT 2022
Updated bio for user 3
Mon Mar 28 22:51:04 PDT 2022
Added user aUsername to the forum.
Mon Mar 28 22:51:25 PDT 2022
Subscribed aUsername to the gaming community.
Mon Mar 28 22:51:30 PDT 2022
aUsername added This is a Laptop to their disliked posts.
Mon Mar 28 22:51:36 PDT 2022
aUsername removed This is a Laptop from their disliked posts.
Mon Mar 28 22:51:36 PDT 2022
aUsername added This is a Laptop to their liked posts.
Mon Mar 28 22:51:37 PDT 2022
aUsername removed This is a Laptop from their liked posts.
Mon Mar 28 22:52:07 PDT 2022
Unsubscribed aUsername from the gaming community.
Mon Mar 28 22:52:34 PDT 2022
Event log cleared.

## Phase 4: Task 3

- I would like to remove the association between CommentsDisplay and Comment, as this can be achieved by just getting the comments from the post field of the CommentsDisplay class
- Instead of having so many classes with an association to PostIt, I would like to instead use the Java Observer/Observable classes or create my own classes in order to implement an Observer pattern, so that the dependent classes can receive updates
- 

