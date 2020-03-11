package ru.otus.chernovsa.atm;

import ru.otus.chernovsa.atm.money.Banknote;
import ru.otus.chernovsa.atm.money.CurrencyCode;
import ru.otus.chernovsa.atm.money.NominalValue;
import ru.otus.chernovsa.atm.money.PutTakeMoney;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Atm implements PutTakeMoney {
    private List<CellOfMoney> cellsOfMoney = new ArrayList<>();

    //переменные для пользовательского сеанса
    private Account usersAccount;
    private List<Banknote> usersBanknotes = new ArrayList<>();

    public Atm() {
        this.cellsOfMoney.add(new CellOfMoney(NominalValue.ONE));
        this.cellsOfMoney.add(new CellOfMoney(NominalValue.TWO));
        this.cellsOfMoney.add(new CellOfMoney(NominalValue.FIVE));
        this.cellsOfMoney.add(new CellOfMoney(NominalValue.TEN));
    }

    public Atm(List<CellOfMoney> cells) {
        cellsOfMoney = new ArrayList<>(cells);
    }

    @Override
    //добавляет одну купюру в купюроприемник
    public boolean addBanknote(Banknote banknote) {
        this.usersBanknotes.add(banknote);
        return true;
    }

    @Override
    //можно удалять только купюры из купюроприемника
    public boolean removeBanknote(NominalValue nominal) {
        if (this.usersBanknotes.isEmpty()
                || this.usersBanknotes.stream().noneMatch(banknote -> banknote.getNominal() == nominal)) {
            return false;
        }

        for (int i = 0; i < usersBanknotes.size(); i++) {
            if (usersBanknotes.get(i).getNominal() == nominal) {
                usersBanknotes.remove(i);
                break;
            }
        }
        return true;
    }

    //банкомат принимает только купюры одной и той же валюты
    @Override
    public boolean putMoney(List<Banknote> banknotes) {
        if (!isSameCurrency(banknotes)) {
            System.out.println("В пачке имеются купюры РАЗНЫХ валют. Можно положить только купюры одной валюты.");
            return false;
        }
        this.usersBanknotes.addAll(banknotes);
        return true;
    }

    //указывает счет, с которым надо будет провести операцию
    public boolean enterAccountNumber(Account account) {
        if (account == null) {
            return false;
        }
        this.usersAccount = account;
        return true;
    }

    public boolean transferMoneyToAccount() {
        if (usersBanknotes == null || usersBanknotes.size() == 0) {
            System.out.println("Операция невозможна: вы НЕ внесли денег в банкомат.");
            return false;
        } else if (usersAccount == null) {
            System.out.println("Операция невозможна: НЕ указан счет для проведения операции.");
            return false;
        } else if (!checkFreeSpaceInCells()) {
            System.out.println("Операция невозможна: банкомат НЕ принимает такие купюры или уменьшите количество купюр.");
            return false;
        }
        //раскладываем банкноты по ячейкам
        Map<NominalValue, List<Banknote>> groupedBanknotes = usersBanknotes.stream().collect(Collectors.groupingBy(Banknote::getNominal));
        for (NominalValue nominalValue : groupedBanknotes.keySet()) {
            int idxCell = findCellIndexByNominalAndCurrency(nominalValue, groupedBanknotes.get(nominalValue).get(0).getCurrency());
            groupedBanknotes.get(nominalValue).forEach(banknote -> cellsOfMoney.get(idxCell).addBanknote(banknote));
        }
        //пополняем счет
        Integer amount = usersBanknotes.stream().map(Banknote::getCost).reduce(Integer::sum).get();
        usersAccount.addMoney(amount);
        usersBanknotes = null;
        return true;
    }

    //проверяем, имеется ли в банкомате место под купюры такого номинала и такой валюты
    public boolean checkFreeSpaceInCells() {
        if (usersBanknotes == null || usersBanknotes.size() == 0) {
            return false;
        }
        //группируем банкноты по номиналу и проверяем
        Map<NominalValue, List<Banknote>> groupedBanknotes = usersBanknotes.stream().collect(Collectors.groupingBy(Banknote::getNominal));

        //проверяем, что в банкомате имеется ячейка с таким номиналом и валютой
        for (NominalValue nominal : groupedBanknotes.keySet()) {
            int cellIndexFound = findCellIndexByNominalAndCurrency(nominal, groupedBanknotes.get(nominal).get(0).getCurrency());
            if (cellIndexFound < 0) {
                System.out.println("Банкомат НЕ принимает банкноты номиналом " + nominal.getValue()
                        + " " + groupedBanknotes.get(nominal).get(0).getCurrency());
                return false;
            }
            Map<Boolean, Integer> checkCell = checkFreeSpaceInOneCell(cellIndexFound, groupedBanknotes.get(nominal).size());
            if (!checkCell.isEmpty() && checkCell.containsKey(false)) {
                System.out.println("Банкомат может принять только " + checkCell.values().toArray()[0]
                        + " банкнот номиналом " + nominal.getValue() + " "
                        + groupedBanknotes.get(nominal).get(0).getCurrency());
                return false;
            }
        }
        return true;
    }

    public Integer getAccountBalance() {
        if (this.usersAccount == null) {
            System.out.println("Операция невозможна: НЕ указан счет для проведения операции.");
            return 0;
        }
        return this.usersAccount.getBalance();
    }

    /**
     * Реализация предполагает следующие допущения:
     * 1) не учитывается запрашиваемый код валюты
     * 2) не может быть несколько ячеек с одинаковым номиналом банкнот
     */
    @Override
    public List<Banknote> takeMoney(int amount) throws Exception {
        List<Banknote> result = new ArrayList<>();
        //проверяем, а указан ли счет и имеется ли на нем запрашиваемая сумма
        if (this.usersAccount == null) {
            System.out.println("Операция невозможна: НЕ указан счет для проведения операции.");
            return result;
        }
        //проверяем, достаточно ли денег на счете
        if (getAccountBalance() < amount) {
            System.out.println("Операция невозможна: на счете недостаточно денег.");
            return result;
        }
        //проверяем, а есть ли вообще в банкомате столько денег
        if (amount > getAtmBalance()) {
            System.out.println("Операция невозможна: в банкомате недостаточно денег.");
            return result;
        }

        //Если счет указан, и денег достаточно на счете и в банкомате
        //сортируем ячейки по номиналу в обратном порядке, т.е. от большого к малому
        cellsOfMoney.sort((o1, o2) -> Integer.compare(
                o2.getNominalValue().getValue(),
                o1.getNominalValue().getValue())
        );
        //определяем, на какие номиналы раскладывается запрошенная сумма
        Map<NominalValue, Integer> decomposedAmountAtNominal = new HashMap<>();
        int tmpAmount = 0;
        for (int k = 0; k < cellsOfMoney.size(); k++) {
            tmpAmount = amount;
            for (int i = k; i < cellsOfMoney.size(); i++) {
                CellOfMoney currentCell = cellsOfMoney.get(i);
                int currentNominal = currentCell.getNominalValue().getValue();
                //вычисляем кол-во банкнот текущего номинала, если запрашиваемая сумма на него делится
                int cnt = tmpAmount / currentNominal;
                int bookedCell = currentCell.getBookedElements();
                bookedCell = Math.min(cnt, bookedCell);
                if (bookedCell > 0) {
                    decomposedAmountAtNominal.put(currentCell.getNominalValue(), bookedCell);
                    tmpAmount -= bookedCell * currentNominal;
                }
            }
            if (tmpAmount == 0) {
                break;
            }
            decomposedAmountAtNominal.clear();
        }
        //если запрошенная сумма не раскладывается на имеющиеся номиналы банкнот в банкомате
        if (tmpAmount > 0) {
            throw new Exception("Невозможно выдать запрошенную сумму! Введите другую сумму.");
        }
        //удалить банкноты из ячеек и сформировать выдачу
        for (Map.Entry<NominalValue, Integer> entry : decomposedAmountAtNominal.entrySet()) {
            NominalValue nominal = entry.getKey();
            int cntBanknotes = entry.getValue();
            for (int i = 0; i < cntBanknotes; i++) {
                result.add(new Banknote(nominal, CurrencyCode.RUB));
            }
            cellsOfMoney.get(findCellIndexByNominalAndCurrency(nominal, CurrencyCode.RUB)).removeBanknotes(cntBanknotes);
        }

        //снимаем деньги со счета
        this.usersAccount.takeMoney(amount);

        return result;
    }

    public void exit() {
        usersAccount = null;
        usersBanknotes.clear();
    }

    private Integer getAtmBalance() {
        return cellsOfMoney.stream().map(c1 -> c1.getNominalValue().getValue() * c1.getBookedElements())
                .reduce(Integer::sum).get();
    }

    private Map<Boolean, Integer> checkFreeSpaceInOneCell(int indexOfCell, int numberBanknotes) {
        Map<Boolean, Integer> result = new HashMap<>();
        int freeSpace = cellsOfMoney.get(indexOfCell).getFreeCapacity();
        boolean canPut = freeSpace >= numberBanknotes;
        result.put(canPut, freeSpace);
        return result;
    }

    private int findCellIndexByNominalAndCurrency(NominalValue cellNominal, CurrencyCode currencyCode) {
        int result = -1;
        for (int i = 0; i < cellsOfMoney.size(); i++) {
            if (cellsOfMoney.get(i).getNominalValue().equals(cellNominal)
                    && cellsOfMoney.get(i).getCurrencyCode().equals(currencyCode)) {
                result = i;
                break;
            }
        }
        return result;
    }
}
