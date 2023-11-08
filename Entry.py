import json
import uuid

class Entry:
    def __init__(self, perso, pro, date):
        self.personnal = perso
        self.professionnal = pro
        self.day = int(date.split('/')[0])
        self.month = int(date.split('/')[1])
        self.year = int(date.split('/')[2])
        self.id = int(uuid.uuid4())

    def dictify(self):
        # return { 
        #     self.id : {
        #         "personnal" : self.personnal,
        #         "professionnal" : self.professionnal,
        #         "day" : self.day,
        #         "month" : self.month,
        #         "year" : self.year
        #     }
        # }
        return {
                "personnal" : self.personnal,
                "professionnal" : self.professionnal,
                "day" : self.day,
                "month" : self.month,
                "year" : self.year
            }
    
list1 = [1, 2, 3, 4, 5]
list2 = [123, 234, 456]
d = {'a': [], 'b': []}
d['a'].append(list1)
d['a'].append(list2)