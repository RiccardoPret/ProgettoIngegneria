import random
stabs=['Bagno Maria','Bagno Sapone','Stabilimento Gold','Stabilimento Silver','Stabilimento Bronze']


#genera ricavi mensili
for y in range(2012,2015):
    for m in range(5,9):
        for s in stabs:
            print "("+str(y)+","+str(m)+",\'"+s+"\',"+str(random.randint(500,7500))+"),"
'''
'''
#genera posti spiaggia
for s in stabs:
    sdraioperfila=random.randint(5,11)
    for n in range(1,random.randint(20,101)):
        print "("+str(n)+",\'"+s+"\',"+str((n/sdraioperfila)+1)+","+str(random.randint(1,5))+"),"