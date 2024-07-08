class Trade:
    def __init__(self, firstName, lastName, transactionDate, owner, assetDescription, type, amount, link, dateRecieved, ticker):
        self.firstName = firstName
        self.lastName = lastName
        self.transactionDate = transactionDate
        self.owner = owner
        self.assetDescription = assetDescription
        self.type = type
        self.amount = amount
        self.link = link
        self.dateRecieved = dateRecieved
        self.ticker = ticker

def parse_senate_trading(data):
    trades = []
    for item in data:
        trade = Trade(
            firstName=item.get("firstName"),
            lastName=item.get("lastName"),
            transactionDate=item.get("transactionDate"),
            owner=item.get("owner"),
            assetDescription=item.get("assetDescription"),
            type=item.get("type"),
            amount=item.get("amount"),
            link=item.get("link"),
            dateRecieved=item.get("dateRecieved"),
            ticker=item.get("symbol")
        )
        trades.append(trade.__dict__)  # Convert Trade object to dictionary
    return trades

def parse_senate_disclosure(data):
    trades = []
    for item in data:
        trade = Trade(
            firstName=item.get("representative").split(" ", 1)[0],
            lastName=item.get("representative").split(" ", 1)[1],
            transactionDate=item.get("transactionDate"),
            owner=item.get("owner"),
            assetDescription=item.get("assetDescription"),
            type=item.get("type"),
            amount=item.get("amount"),
            link=item.get("link"),
            dateRecieved=item.get("disclosureDate"),
            ticker=item.get("ticker")
        )
        trades.append(trade.__dict__)  # Convert Trade object to dictionary
    return trades