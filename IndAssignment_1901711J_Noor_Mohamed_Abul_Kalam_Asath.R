my_dataset<-read.csv("D:/TP Training Material/Year 2/Semsester 1/Data Science Essential/working_dataset.csv", header=TRUE, sep=",")


library(dplyr)

library(ggplot2)

age_sex <-my_dataset %>% select(Casualty_Class, Sex_of_Driver, Age_of_Driver)
#Selected Casualty_Class, sex_of_Driver and Age_of driver  in a new data frame called age_sex to create a bar chart

age_sex <- age_sex %>% mutate(Age_of_Driver = na_if(Age_of_Driver , "-1"))
# Change outiers -1 to na to remove them

age_sex <-na.omit(age_sex)
#removed na values in age_sex

age_sex = age_sex %>% filter(Casualty_Class == "Driver")
#selected rows with only drivers as the casualty class as i dont want pedestrian or passanger class to distort the data

age_sex_bar<-ggplot(age_sex,aes(Age_of_Driver,fill=Sex_of_Driver)) + geom_bar(stat = "count",position = position_dodge()) + xlim(10, 70) + xlab('Age Of the Driver')+ggtitle('Number of people involved in Accidents based on gender and age')
#I used sex_of_Driver as fill and used count to coun tthe number of accident that occured.
age_sex_bar

total_week <-my_dataset %>% group_by(Accident_Index)%>% summarise(weekday = Day_of_Week[1]) %>% count(weekday)
#To calculate on which day does the most accident occure on i created a new data frame called totalweek.
#grouping the dataset by Accident_Index, i count the number of day in a week the accident occurs.

total_week$weekday <-factor(total_week$weekday, level = c("Monday", "Tuesday", "Wednesday","Thursday", "Friday", "Saturday","Sunday"))
#I provide level for the weekday column i created  strating from monday and ending on sundary. Provide an ordered overview of the data

ggplot(total_week,aes(weekday, n, label = n))+geom_bar(stat='identity')+geom_text(size = 6,vjust = 2, color = "white")
# I created a bar chart to show which day of the week does most accident occures. I also added the values on top of the chart

Date_asc <- my_dataset[!duplicated(my_dataset$Accident_Index),]

Accident_id_date <- my_dataset %>% group_by(Date) %>% summarise(Total_Accident = n())

Accident_id_date$Date<- as.Date(Accident_id_date$Date,"%d/%m/%Y") #change date type from factor to date format

Accident_id_date<- Accident_id_date[order(Accident_id_date$Date),] #so rt the date 


Accident_in_Day <- my_dataset %>% group_by(Date) %>% summarize(Accident_in_Day = sum(Number_of_Accidents))
#calculated numbe rof accidents
Accident_in_Month <-Accident_in_Day %>% mutate(Month = format(Date, "%Y-%m"))
#I changed the format of the month to %Y-%m
 
Accident_in_Month2 <- Accident_in_Month %>% group_by(Month) %>% summarise(Accident_in_Month = sum(Accident_in_Day))

ggplot(Accident_in_Month2,aes(Month,Accident_in_Month))+geom_histogram(stat = "identity",color='darkgrey',fill='red')+labs(x='Month',y='Number of Accident In Month',title="Number Of Accident occured based on Months")

Number_of_Accident <-my_dataset %>% group_by(Accident_Index) %>% summarise(Number_of_Accidents = n())


model<-my_dataset  #created a new dataframe for modeling

model$Junction_Control<-as.character(model$Junction_Control) # Changed Junction control type to character

model$Junction_Control[is.na(model$Junction_Control)]<-"Not a junction" #made all the na values in Junction Control to Not a junction

model$Junction_Control<-as.factor(model$Junction_Control) # change it back to factor
 
model <- model %>% mutate(Age_of_Driver = na_if(Age_of_Driver , "-1")) 


model2<-model #make a copy of model to make sure i dont make any error

model2$time2<-format(as.POSIXct(model2$Time, tz = "" , format = "%H: %M"), "%H") # I changed data in Time column into POSIXct format %H:%M 
#and seperated hours to calculate day cycle like morning,afternoon,evening ,night


str(model2$time2) # we can see that time2 column in character

model2$time2<-as.numeric(model2$time2) # we change time2 column to numberic

model2$time_category <- ifelse(model2$time2 >= 05 & model2$time2 <= 11, "Morning",ifelse(model2$time2 > 11 & model2$time2 <= 16, "Afternoon",ifelse(model2$time2 > 16 & model2$time2 <= 19, "Evening", "Night")))
# I have created a column in model2 dataframe called time_category where we can see the day cycle etc,. if time2 column value is more then or equal to 5
#and it s less then or equal to 11 it is morning.

model<-model2 #changed it back to model

model$time_category <-as.factor(model$time_category) #chaged time_category type into Factor for logistic regression later


day_of_week <-model%>%select(Casualty_Class,time_category) #

 # As of before i just wanted to calculate accidents based only on drivers so i excluded passengers and pedestrians

day_of_week2$time_category <-factor(day_of_week2$time_category, level = c("Morning", "Afternoon", "Evening","Night")) # created level for time_category variable

ggplot(day_of_week2,aes(time_category))+geom_histogram(stat="count",fill="orange")+labs(x='Number of Accidents', y='Period of the Day', title = "Accident Occurence based on period of the day")
#created a histogram to show how many accidents occured based on period of the day. Used stat as count

ggplot(model,aes(Casualty_Class,fill=Accident_Severity))+geom_bar(stat="count",position = 'fill')+labs(x ="Classs of Casualty",y="Proportion of Accident Severity",title = "Proportion of severity based on Class of the Casualty")
#Another bar graph show the proportion of accident severity based on class of casualty

logistic<-glm(Accident_Severity ~ Weather_Conditions+Road_Surface_Conditions+time_category, data=model,family = binomial(link="logit"))
# Created a regression model using weather_conditon,road_surface condition and time_category data attribute

fitted.result<-predict(logistic,newdata=m,type='response')
# used logistic to created a fitted.result.
hist(fitted.result,main="Probability of Accident Severity",xlab = "prob",ylab="count",col="purple")
#histogram of fitted result

k<-model%>%select(Number_of_Casualties,Age_of_Driver,Engine_Capacity_CC)
#selected some attributes to do k mean cluster

k<-na.omit(k)
#omited na values in k data frame

kmc<-kmeans(k,3)
#we clustered into 3 groups

k<-k%>%mutate(cluster=kmc$cluster)
# created a new column to store cluster value
plot(Age_of_Driver~Engine_Capacity_CC,k,col=kmc$cluster)
#ploting the k-mean cluster



