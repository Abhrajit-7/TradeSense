document.addEventListener('DOMContentLoaded', () => {
    const jwtToken = localStorage.getItem('jwtToken');
    const numberButtonsContainer = document.getElementById('number-buttons');
    const amountInput = document.getElementById('amount');
    const submitButton = document.getElementById('submit-button');
    var balance = sessionStorage.getItem('acctBal');
    const username = getUsernameFromLocalStorage();
    const id=localStorage.getItem('myId');
    const dataContainer = document.getElementById('data-container');

    const stockButtonsContainer = document.getElementById('stock-buttons-container');

    //Adding toggle buttons
    const slotButtons = document.querySelectorAll('.slot-button');
    const switchSlider = document.querySelector('.switch-slider');
    const switchWidth = switchSlider.offsetWidth;
    const containerContents = document.querySelectorAll('.slot-content');

    let currentSlotIndex = 0;
    /*if(!jwtToken){
        window.location.href='/login';
    }*/
    console.log(balance);
    //let selectedNumbers = [];
    // Initialize selected stocks array
    let selectedStocks = [];
    balance = parseFloat(balance);
    document.getElementById('balance').textContent = 'Rs.' + balance.toFixed(2);

   // Example stock names array
   //const stockNames = ['AAPL', 'GOOGL', 'AMZN', 'MSFT', 'TSLA', 'FB', 'NFLX', 'NVDA', 'ADBE', 'PYPL'];
    const stockNames = [
        { name: "Reliance Industries", price: 2400.50, nseCode: "RELIANCE" },
        { name: "Tata Consultancy Services", price: 3500.75, nseCode: "TCS" },
        { name: "HDFC Bank", price: 1600.25, nseCode: "HDFCBANK" },
        { name: "Infosys", price: 1800.30, nseCode: "INFY" },
        { name: "Hindustan Unilever", price: 2500.80, nseCode: "HINDUNILVR" },
        { name: "ICICI Bank", price: 700.45, nseCode: "ICICIBANK" },
        { name: "ITC", price: 220.90, nseCode: "ITC" },
        { name: "Axis Bank", price: 850.60, nseCode: "AXISBANK" },
        { name: "Bharti Airtel", price: 600.10, nseCode: "BHARTIARTL" },
        { name: "Kotak Mahindra Bank", price: 2000.20, nseCode: "KOTAKBANK" },
        { name: "Nestle India", price: 17500.00, nseCode: "NESTLEIND" },
        { name: "Bajaj Finance", price: 5800.75, nseCode: "BAJFINANCE" },
        { name: "Maruti Suzuki", price: 7000.50, nseCode: "MARUTI" },
        { name: "Larsen & Toubro", price: 1600.00, nseCode: "LT" },
        { name: "HCL Technologies", price: 1000.50, nseCode: "HCLTECH" },
        { name: "Sun Pharmaceutical", price: 500.30, nseCode: "SUNPHARMA" },
        { name: "Mahindra & Mahindra", price: 900.75, nseCode: "M&M" },
        { name: "Tata Steel", price: 450.25, nseCode: "TATASTEEL" },
        { name: "Power Grid Corporation", price: 200.10, nseCode: "POWERGRID" },
        { name: "Asian Paints", price: 3000.20, nseCode: "ASIANPAINT" },
        { name: "Hindalco Industries", price: 400.50, nseCode: "HINDALCO" },
        { name: "Adani Ports & SEZ", price: 700.80, nseCode: "ADANIPORTS" },
        { name: "Tech Mahindra", price: 1200.90, nseCode: "TECHM" },
        { name: "Bajaj Finserv", price: 11000.00, nseCode: "BAJAJFINSV" },
        { name: "Wipro", price: 500.40, nseCode: "WIPRO" },
        { name: "Dr. Reddy's Laboratories", price: 4800.00, nseCode: "DRREDDY" },
        { name: "SBI Life Insurance", price: 1000.70, nseCode: "SBILIFE" },
        { name: "Shree Cement", price: 25000.50, nseCode: "SHREECEM" },
        { name: "Britannia Industries", price: 3800.00, nseCode: "BRITANNIA" },
        { name: "UltraTech Cement", price: 7000.30, nseCode: "ULTRACEMCO" },
        { name: "Divi's Laboratories", price: 4200.75, nseCode: "DIVISLAB" },
        { name: "NTPC", price: 150.20, nseCode: "NTPC" },
        { name: "Coal India", price: 150.80, nseCode: "COALINDIA" },
        { name: "Eicher Motors", price: 2700.60, nseCode: "EICHERMOT" },
        { name: "IndusInd Bank", price: 900.75, nseCode: "INDUSINDBK" },
        { name: "Tata Consumer Products", price: 700.30, nseCode: "TATACONSUM" },
        { name: "HDFC Life Insurance", price: 700.45, nseCode: "HDFCLIFE" },
        { name: "Godrej Consumer Products", price: 900.20, nseCode: "GODREJCP" },
        { name: "JSW Steel", price: 700.30, nseCode: "JSWSTEEL" },
        { name: "Adani Green Energy", price: 1200.40, nseCode: "ADANIGREEN" },
        { name: "Havells India", price: 1200.20, nseCode: "HAVELLS" },
        { name: "ICICI Prudential Life", price: 500.70, nseCode: "ICICIPRULI" },
        { name: "Cipla", price: 1000.30, nseCode: "CIPLA" },
        { name: "Berger Paints", price: 750.40, nseCode: "BERGEPAINT" },
        { name: "Pidilite Industries", price: 2100.75, nseCode: "PIDILITIND" },
        { name: "DLF", price: 350.20, nseCode: "DLF" },
        { name: "Grasim Industries", price: 1600.80, nseCode: "GRASIM" },
        { name: "Hero MotoCorp", price: 3000.50, nseCode: "HEROMOTOCO" },
        { name: "Bharat Petroleum", price: 400.25, nseCode: "BPCL" },
        { name: "Siemens", price: 2200.60, nseCode: "SIEMENS" },
        { name: "Zee Entertainment", price: 300.30, nseCode: "ZEEL" },
        { name: "SRF", price: 7000.80, nseCode: "SRF" },
        { name: "UPL", price: 700.50, nseCode: "UPL" },
        { name: "Hindustan Zinc", price: 300.40, nseCode: "HINDZINC" },
        { name: "Dabur India", price: 600.20, nseCode: "DABUR" },
        { name: "Motherson Sumi Systems", price: 150.25, nseCode: "MOTHERSUMI" },
        { name: "Page Industries", price: 40000.00, nseCode: "PAGEIND" },
        { name: "Ambuja Cements", price: 300.70, nseCode: "AMBUJACEM" },
        { name: "Bosch", price: 16000.30, nseCode: "BOSCHLTD" },
        { name: "Torrent Pharmaceuticals", price: 3000.60, nseCode: "TORNTPHARM" },
        { name: "Bata India", price: 1600.25, nseCode: "BATAINDIA" },
        { name: "Marico", price: 500.70, nseCode: "MARICO" },
        { name: "Adani Total Gas", price: 1500.50, nseCode: "ATGL" },
        { name: "Voltas", price: 900.30, nseCode: "VOLTAS" },
        { name: "GAIL India", price: 150.50, nseCode: "GAIL" },
        { name: "Oil and Natural Gas Corporation", price: 125.20, nseCode: "ONGC" },
        { name: "Glenmark Pharmaceuticals", price: 600.40, nseCode: "GLENMARK" },
        { name: "TVS Motor Company", price: 800.30, nseCode: "TVSMOTOR" },
        { name: "Manappuram Finance", price: 150.70, nseCode: "MANAPPURAM" },
        { name: "Indian Oil Corporation", price: 100.20, nseCode: "IOC" },
        { name: "Crompton Greaves", price: 400.60, nseCode: "CROMPTON" },
        { name: "BEML", price: 1200.50, nseCode: "BEML" },
        { name: "BEL", price: 150.40, nseCode: "BEL" },
        { name: "Aurobindo Pharma", price: 700.70, nseCode: "AUROPHARMA" },
        { name: "Amara Raja Batteries", price: 800.50, nseCode: "AMARAJABAT" },
        { name: "ABB India", price: 2500.40, nseCode: "ABB" },
        { name: "ICICI Lombard", price: 1200.30, nseCode: "ICICIGI" },
        { name: "Max Financial Services", price: 800.70, nseCode: "MFSL" },
        { name: "IDFC First Bank", price: 60.20, nseCode: "IDFCFIRSTB" },
        { name: "Lupin", price: 700.40, nseCode: "LUPIN" },
        { name: "Ashok Leyland", price: 150.50, nseCode: "ASHOKLEY" },
        { name: "United Breweries", price: 1500.70, nseCode: "UBL" },
        { name: "PVR", price: 1200.30, nseCode: "PVR" },
        { name: "Jubilant FoodWorks", price: 1000.50, nseCode: "JUBLFOOD" },
        { name: "InterGlobe Aviation", price: 1800.60, nseCode: "INDIGO" },
        { name: "Muthoot Finance", price: 1500.70, nseCode: "MUTHOOTFIN" },
        { name: "HDFC Asset Management", price: 3000.50, nseCode: "HDFCAMC" },
        { name: "ICICI Securities", price: 400.70, nseCode: "ISEC" },
        { name: "AU Small Finance Bank", price: 800.50, nseCode: "AUBANK" },
        { name: "Astral", price: 2000.60, nseCode: "ASTRAL" },
        { name: "Balkrishna Industries", price: 1500.70, nseCode: "BALKRISIND" },
        { name: "Biocon", price: 400.50, nseCode: "BIOCON" },
        { name: "Adani Power", price: 100.30, nseCode: "ADANIPOWER" },
        { name: "Escorts", price: 1500.70, nseCode: "ESCORTS" },
        { name: "Indraprastha Gas", price: 500.40, nseCode: "IGL" },
        { name: "Aditya Birla Fashion", price: 200.60, nseCode: "ABFRL" }
    ];

   // Function to create a stock card
   function createStockCard(stock) {
       const stockCard = document.createElement('div');
       stockCard.classList.add('stock-card');
       stockCard.id = `card-${stock.nseCode}`; // Add ID for easy selection

       const stockName = document.createElement('div');
       stockName.classList.add('stock-name');
       stockName.textContent = stock.name;

       const stockNseCode = document.createElement('div');
       stockNseCode.classList.add('stock-nse-code');
       stockNseCode.textContent = `${stock.nseCode}`;

       const stockPrice = document.createElement('div');
       stockPrice.classList.add('stock-price');
       stockPrice.textContent = `Rs. ${stock.price.toFixed(2)}`;

       const stockActions = document.createElement('div');
       stockActions.classList.add('stock-actions');

       const stockQuantity = document.createElement('input');
       stockQuantity.type = 'number';
       stockQuantity.min = 1;
       stockQuantity.value = 1;
       stockQuantity.classList.add('stock-quantity');

       stockQuantity.addEventListener('change', () => updateQuantity(stock, stockQuantity.value));

       const selectButton = document.createElement('button');
       selectButton.classList.add('button');
       selectButton.textContent = 'Buy';
       selectButton.addEventListener('click', () => toggleStockSelection(stock, stockPrice.textContent, stockCard, stockQuantity.value));

       stockActions.appendChild(selectButton);
       stockActions.appendChild(stockQuantity);

       stockCard.appendChild(stockName);
       stockCard.appendChild(stockNseCode);
       stockCard.appendChild(stockPrice);
       stockCard.appendChild(stockActions);

       stockButtonsContainer.appendChild(stockCard);
   }

   // Function to update the quantity of a selected stock
   function updateQuantity(stock, quantity) {
       const selectedStock = selectedStocks.find(item => item.stock.nseCode === stock.nseCode);
       if (selectedStock) {
           selectedStock.quantity = parseInt(quantity);
       }
   }

   // Function to handle stock selection
   function toggleStockSelection(stock, price, card, quantity) {
       const index = selectedStocks.findIndex(item => item.stock.nseCode === stock.nseCode);
       if (index === -1) {
           selectedStocks.push({ stock: stock, price: price, quantity: parseInt(quantity) });
           card.classList.add('selected');
       } else {
           selectedStocks.splice(index, 1);
           card.classList.remove('selected');
       }
   }

   // Initialize stock cards
   stockNames.forEach(stock => createStockCard(stock));


    // Event listener for submit button
    submitButton.addEventListener('click', () => {
        if (selectedStocks.length === 0) {
            alert('Please select at least one stock');
            return;
        }
        if (selectedStocks.length !== 0) {
            const bet_number = generateBetId();
                const totalAmount = selectedStocks.reduce((sum, item) => {
                    const price = parseFloat(item.stock.price);
                    const quantity = parseInt(item.quantity, 10);
                    console.log(`Price: ${price}, Quantity: ${quantity}`);
                    if (isNaN(price) || isNaN(quantity)) {
                        console.error('Invalid data:', item);
                        return sum;
                    }
                    return sum + (price * quantity);
                }, 0);
                console.log(totalAmount);
            if (totalAmount <= balance) {
                // Send data to backend
                if (totalAmount !== 0) {
                    const selectedStockNseCodes = selectedStocks.map(stock => stock.stock.nseCode).join(',');
                    const selectedStockPrices = selectedStocks.map(stock => parseFloat(stock.price));
                    const selectedStockQuantities = selectedStocks.map(stock => stock.quantity);

                    const data = {
                        bet_number: bet_number,
                        selected_stock_nse_codes: selectedStockNseCodes,
                        selected_stock_prices: selectedStockPrices,
                        selected_stock_quantities: selectedStockQuantities,
                        bet_amount: totalAmount,
                        numberCount: selectedStocks.length,
                        username: username,
                        slot: "Slot-"+(currentSlotIndex+1)
                    };

                    // Function to fetch data for the selected slot

                        let endpoint = '';
                        if (currentSlotIndex === 0) {
                            endpoint = 'http://ec2-13-201-33-242.ap-south-1.compute.amazonaws.com/api/v1/submit/slot1/'+username;
                        } else if (currentSlotIndex === 1) {
                            endpoint = 'http://ec2-13-201-33-242.ap-south-1.compute.amazonaws.com/api/v1/submit/slot2/'+username;
                        }

                        fetch(endpoint, {
                            method: 'POST',
                            headers: {
                                'Authorization': `Bearer ${jwtToken}`,
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify(data),
                        })
                        .then(response => {
                            if (!response.ok) {
                                throw new Error('Network response was not ok');
                            }
                            return response.json();
                        })
                        .then(data => {
                            console.log('Data sent successfully:', data);
                            // Updated balance
                            balance -= totalAmount;
                            // Set the balance in a session
                            sessionStorage.setItem('acctBal', balance);
                            console.log("Balance after deduction: " + balance);
                            balance.textContent = balance.toFixed(2);
                            console.log("Balance after deduction: " + balance);
                            document.getElementById('balance').textContent = 'Rs.' + balance.toFixed(2);
                            console.log(`ID: ${bet_number}, Selected stocks: ${selectedStockNseCodes}, Amount per number: Rs.${selectedStockPrices}, Total amount: Rs.${totalAmount.toFixed(2)}, New balance: Rs. ${balance.toFixed(2)}`);
                            console.log(`Username: ${username}`);
                            // You can perform further actions here, such as showing a success message to the user
                            alert('Your selections were successfully submitted.');
                            console.log(currentSlotIndex+1);
                            addNewEntry(data, currentSlotIndex+1);

                        })
                        .catch(error => {
                            console.error('There was a problem with your fetch operation:', error);
                            // Handling errors here
                            alert('Oops! Something went wrong. Please try again later..');
                            dataContainer.innerHTML = '<p>Failed to fetch data. Please try again later.</p>';
                        });

                }
                // Reset selections
                selectedStocks = [];
                const buttons = stockButtonsContainer.querySelectorAll('button');
                buttons.forEach(button => {
                    button.classList.remove('selected');
                });
            } else {
                alert('Insufficient balance');
                return false;
            }
        }
});

    // On loading the page, set the username and render the data
    window.onload = function() {
        setUsernameInPage();
        // localStorage.getItem('cachedData');
        let onLoadData = JSON.parse(localStorage.getItem('cachedData1'));
        if(onLoadData !== null){
        renderData(onLoadData, currentSlotIndex+1);
        }
        //getList('1');
    };

    function getList(slot){
        fetch('http://ec2-13-201-33-242.ap-south-1.compute.amazonaws.com/api/v1/lists/'+ username +'?slot=Slot-'+slot)
        .then(response => response.json())
        .then(data => {
            // Store data in localStorage
            localStorage.setItem('cachedData1', JSON.stringify(data));
            // Render data to the UI
            renderData(data, slot);
        })
        .catch(error => console.error('Error fetching data:', error));
    }

    // Function to add a new entry
    function addNewEntry(newEntry, slotName) {
        console.log('Slot: ', slotName);
        // Update client-side cache (localStorage) with new entry
        let cachedData = JSON.parse(localStorage.getItem('cachedData' + slotName)) || [];
        console.log('Cached Data:', cachedData);
        cachedData.push(newEntry);
        localStorage.setItem('cachedData' + slotName, JSON.stringify(cachedData));
        renderData(cachedData, slotName); // Update UI with new entry
    }

    // Function to render data to the UI
    function renderData(data, slotName) {
        const dataList = document.getElementById(`data-container-slot-${slotName}`);
        dataList.innerHTML = ''; // Clear previous content
        // Render data to the UI
        data.slice().slice(0, 10).forEach(entry => {
            const listItem = document.createElement('li');
            listItem.textContent = entry;
            dataList.appendChild(listItem);
        });
    }

   // Function for toggling
       function updateSlot(index) {
           const currentSlot = parseInt(slotButtons[index].dataset.slot);
           console.log('Current Slot:', currentSlot);
           //alert("Slot " + currentSlot);

           // Highlight the selected slot and dim the other slot buttons
           slotButtons.forEach((button, idx) => {
               if (idx === index) {
                   button.classList.add('active');
               } else {
                   button.classList.remove('active');
               }
         });

           // Hide all containers
           containerContents.forEach(container => container.style.display = 'none');

           // Show the container corresponding to the selected slot
           const containerId = `slot-${currentSlot}`;
           const selectedContainer = document.getElementById(containerId);
           if (selectedContainer) {
               selectedContainer.style.display = 'block';
               // Fetch and render data for the selected slot
               getList(currentSlot);
           }
       }

       // Initial update of the slot
       updateSlot(currentSlotIndex);

       slotButtons.forEach((button, index) => {
           button.addEventListener('click', function() {
           const buttonIndex = Array.from(slotButtons).indexOf(this);
           const buttonWidth = this.offsetWidth;
           switchSlider.style.transform = `translateX(${buttonIndex * buttonWidth}px)`;
           currentSlotIndex = buttonIndex;
           updateSlot(currentSlotIndex);
           });
       });
    function generateBetId() {
        const timestamp = new Date().getTime(); // Current timestamp
        return `${timestamp} `;
    }
});

function getUsernameFromLocalStorage() {
    return localStorage.getItem("username");
}

// Function to set username in HTML content
function setUsernameInPage() {
    const username = getUsernameFromLocalStorage();
    const id=localStorage.getItem("myId");
    if (username) {
        document.getElementById('username').innerText = username;
        document.getElementById('id').innerText = id;
    } else {
        // Handle the case when username is not found in localStorage
        document.getElementById('username').innerText = "Guest";
    }
}

// Call function to set username when page loads
window.onload = function() {
    setUsernameInPage();
    // localStorage.getItem('cachedData');
    let onLoadData = JSON.parse(localStorage.getItem('cachedData1'));
    renderData(onLoadData, currentSlotIndex+1);
};

// Function to handle logout
function logout() {
    fetch('http://ec2-13-201-33-242.ap-south-1.compute.amazonaws.com/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (response.ok) {
            localStorage.removeItem('jwtToken');
            localStorage.removeItem('cachedData1');
            localStorage.removeItem('cachedData2');
            // Redirect to the login page after successful logout
            window.location.href = '/login?logout';
        } else{
            console.error('Logout failed');
        }
    })
    .catch(error => {
        console.error('Error during logout:', error);
    });
};

// Connect to WebSocket server
var stompClient = null;
var socket = new SockJS('/ws');
stompClient = Stomp.over(socket);
stompClient.connect({}, function(frame) {
    console.log(frame);
    stompClient.subscribe('/all/messages', function(result) {
        showMessage(JSON.parse(result.body));
    });
    stompClient.subscribe('/all/slot2', function(result2) {
        showMessage(JSON.parse(result2.body));
    });
});
