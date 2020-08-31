 int start=0;
void setup() {
  // put your setup code here, to run once:
 
Serial.begin(9600);
pinMode(13,OUTPUT);
pinMode(A0,INPUT);
pinMode(7,OUTPUT);
digitalWrite(7,HIGH);
digitalWrite(13,HIGH);
}

void loop() {
  // put your main code here, to run repeatedly:
   if(Serial.available())
{
   char input=Serial.read();
   if(input=='1')
   {
   digitalWrite(13,LOW);
     start=0;
   }
   if(input=='2')
   {
     start=0;
     digitalWrite(13,HIGH);
   }
   if(input=='3')
   {
     start=1;
     

   }
   
}
if(start==1)
{
  int data = analogRead(A0);
  /*int dataa[4];
  for(int i=3;i>=0;i--)
  {
    int temp=data%10;
    data=data/10;
   
    if(data)
     dataa[i]=temp;
    else
    dataa[i]=0;
   
  }
 for(int i=0;i<4;i++)
  {
    Serial.print(dataa[i]);
    delay(500);
  }
  delay(5000);
*/
char c;
  if(data < 341)
     c = 'h';
   else if (data > 341 && data <682)
    c = 'm';
   else
   c = 'k';
  Serial.print(c);
 delay(1000); 
    
     
  

}
}
