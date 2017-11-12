const int pwPin = 7;
long pulse, inches, cm;


void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  pinMode(pwPin, INPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  long sum = 0;
  const int ITERATIONS = 2;
  for(int i=0; i<ITERATIONS; i++)
  {
    sum += pulseIn(pwPin, HIGH);
    delay(10);
  }
  pulse = sum / ITERATIONS;
  inches = pulse / 147;
  cm = inches * 2.54;
  Serial.println(cm);
  delay(500);
}
