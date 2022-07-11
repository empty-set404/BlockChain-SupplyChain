pragma solidity ^0.4.22;

contract MySupplyChain {
    // 收据
    uint private receiptId;
    // 历史收据
    uint private receiptHistoryId;

    // 公司信息结构体
    struct Company {
        string companyName;
        address companyAddress;
        uint creditAsset;
        uint8 companyType; // 0公司 1银行
    }

    // 数字凭证交易历史信息
    struct ReceiptHistory {
        uint rHid; // 凭证历史id
        uint rId; // 凭证id
        address senderAddress;
        address receiverAddress;
        uint amount;
        uint createTime;
    }

    // 数字凭证信息
    struct Receipt {
        uint rId;
        address makerAddress;
    }

    // 公司地址 =》 公司
    mapping(address => Company) private companyMap;

    // 工资地址数组
    address[] private companies;

    // 凭证id =》 凭证
    mapping(uint => Receipt) private receiptMap;
    // 所有凭证数组
    uint[] private receipts;

    // 交易历史map，凭证历史id =》 凭证历史
    mapping(uint => ReceiptHistory) private transHistoryMap;
    // 所有交易历史数组
    uint[] private transHistories;

    // 构造函数
    constructor() public {
        receiptId = 1;
        receiptHistoryId = 1;
    }

    // 生成凭证唯一id
    function getReceiptsId() private returns (uint) {
        return receiptId++;
    }

    // 生成凭证交易历史唯一id
    function getReceiptHistoriesId() private returns (uint) {
        return receiptHistoryId++;
    }

    // 注册新公司
    function addCompany(string name, address companyAddress, uint8 companyType, uint credit) public returns (uint) {
        require(keccak256(companyMap[companyAddress].companyName) == keccak256(""), "500002");
        Company memory newCompany = Company(name, companyAddress, credit, companyType);

        companies.push(companyAddress);
        companyMap[companyAddress] = newCompany;

        return 200;
    }

    function getCompany(address companyAddress) public view returns(string, address, uint, uint8) {
        Company memory company = companyMap[companyAddress];
        return (company.companyName, company.companyAddress, company.creditAsset, company.companyType);
    }

    function getAllCompanyAddress() public view returns(address[]){
        return companies;
    }

    function createReceipt(address makerAddress) public returns(uint) {
        uint rId = getReceiptsId();
        Receipt memory newReceipt = Receipt(rId, makerAddress);
        receiptMap[rId] = newReceipt;
        receipts.push(rId);
        return rId;
    }

    function getAllReceipt() public view returns(uint[]) {
        return receipts;
    }

    function getAllReceiptIds(address _makerAddress) public view returns(uint[] memory) {
        uint[] memory rids = new uint[](getReceiptSize(_makerAddress));
        uint size = 0;

        for(uint i=0; i<receipts.length; i++) {
            Receipt memory receipt = receiptMap[receipts[i]];

            if(receipt.makerAddress == _makerAddress) {
                rids[size] = receipt.rId;
                size++;
            }
        }
        return rids;
    }

    function getReceiptSize(address _makerAddress) private view returns (uint) {
        uint size = 0;
        for (uint i =0; i<receipts.length; i++) {
            if (receiptMap[receipts[i]].makerAddress == _makerAddress) {
                size++;
            }
        }
        return size;
    }

    function getReceiptDetails(uint rid) public view returns(uint, address) {
        Receipt memory receipt = receiptMap[rid];
        return (receipt.rId, receipt.makerAddress);
    }

    function getAllReceiptHistory() public view returns(uint[]) {
        return transHistories;
    }

    function getAllReceiptHostoryIds(uint rid) public view returns(uint[] memory) {
        uint[] memory tranHistoryIds = new uint[](getReceiptHistorySize(rid));

        uint number = 0;
        for(uint i=0; i<transHistories.length; i++) {
            if (transHistoryMap[transHistories[i]].rId == rid) {
                tranHistoryIds[number] = transHistoryMap[transHistories[i]].rHid;
                number++;
            }
        }
        return tranHistoryIds;
    }

    function getReceiptHistorySize(uint rid) private view returns(uint) {
        uint size = 0;
        for(uint i=0;i<transHistories.length;i++){
            if(transHistoryMap[transHistories[i]].rId == rid) {
                size++;
            }
        }

        return size;
    }

    function getReceiptHistoryDetail(uint rhid) public view returns(uint,uint,address,address,uint,uint) {
        ReceiptHistory memory receiptHistory = transHistoryMap[rhid];
        return (receiptHistory.rHid,receiptHistory.rId,receiptHistory.senderAddress,receiptHistory.receiverAddress,receiptHistory.amount,receiptHistory.createTime);
    }

    function  transReceipt(uint rid, address senderAddress, address receiverAddress, uint amount) public returns (uint) {
        require(msg.sender == receiverAddress, "500002");
        require(companyMap[senderAddress].companyAddress != address(0), "404001");
        require(companyMap[receiverAddress].companyAddress != address(0), "404002");
        require(companyMap[receiverAddress].creditAsset >= amount, "500001");

        uint newId = getReceiptHistoriesId();

        ReceiptHistory memory receiptHistory = ReceiptHistory(
            newId,
            rid,
            senderAddress,
            receiverAddress,
            amount,
            now);

        transHistoryMap[newId] = receiptHistory;
        transHistories.push(newId);

        companyMap[senderAddress].creditAsset += amount;
        companyMap[receiverAddress].creditAsset -= amount;

        return 200;
    }
}