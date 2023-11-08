class Entry:
    def __init__(self, perso, pro, date):
        self.personnal = perso
        self.professionnal = pro
        self.day = date.split('/')[0]
        self.month = date.split('/')[1]
        self.year = date.split('/')[2]
