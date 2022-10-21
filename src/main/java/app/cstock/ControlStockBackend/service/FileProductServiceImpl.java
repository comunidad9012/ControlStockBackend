package app.cstock.ControlStockBackend.service;

import app.cstock.ControlStockBackend.dto.FileProductDto;
import app.cstock.ControlStockBackend.entity.Codes;
import app.cstock.ControlStockBackend.entity.FileProduct;
import app.cstock.ControlStockBackend.exeption.ResourceNoteFoundException;
import app.cstock.ControlStockBackend.repository.FileProductRepository;
import app.cstock.ControlStockBackend.tools.CodesTools;
import app.cstock.ControlStockBackend.tools.FileProductTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileProductServiceImpl implements FileProductService {

    @Autowired
    private FileProductRepository fileProductRepository;

    FileProductTools fileProductTools = new FileProductTools();
    CodesTools codesTools = new CodesTools();

    @Override
    public List<FileProductDto> getAllFileProduct() {
        List<FileProduct> fileProductList = fileProductRepository.findAll();
        return fileProductList.stream().map(fileProduct -> fileProductTools.mapDto(fileProduct))
                .collect(Collectors.toList());
    }

    @Override
    public FileProductDto getByIdFileProduct(Long id) {
        FileProduct fileProduct = fileProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNoteFoundException("FileProduct", "id", id));
        return fileProductTools.mapDto(fileProduct);
    }

    @Override
    public FileProduct getByIdFileProductEntity(Long id) {
        FileProduct fileProduct = fileProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNoteFoundException("FileProduct", "id", id));
        return fileProduct;
    }

    @Override
    public Collection<Codes> getAllCodesOfProduct(Long id) {
        FileProduct fileProduct = fileProductRepository.findById(id).orElseThrow();
        if (fileProduct != null) {
            return fileProduct.getCodes();
        } else {
            return null;
        }
    }

    @Override
    public List<FileProductDto> newFileProductList(List<FileProductDto> fileProductDtoList) {

        List<FileProduct> fileProductsListResponse = new ArrayList<>();

        for (FileProductDto i : fileProductDtoList) {
            FileProduct newFile = fileProductTools.mapEntity(i);
            FileProduct saveFileProduct = fileProductRepository.save(newFile);
            fileProductsListResponse.add(saveFileProduct);
        }
        return fileProductsListResponse.stream().map(fileProduct -> fileProductTools.mapDto(fileProduct))
                .collect(Collectors.toList());
    }

    @Override
    public FileProductDto postFileProduct(FileProductDto fileProductDto) {
        FileProduct newFileProduct = fileProductRepository.save(fileProductTools.mapEntity(fileProductDto));
        return fileProductTools.mapDto(newFileProduct);
    }

    @Override
    public FileProductDto updateFileProduct(FileProductDto fileProductDto, Long id) {

        FileProduct fileProduct = getByIdFileProductEntity(id);

        fileProduct.setProductName(fileProductDto.getProductName());
        fileProduct.setMark(fileProductDto.getMark());
        fileProduct.setAmount(fileProductDto.getAmount());

        FileProduct fileProductUpdated = fileProductRepository.save(fileProduct);
        return fileProductTools.mapDto(fileProductUpdated);
    }

    @Override
    public void deleteFileProduct(Long id) {
        fileProductRepository.delete(getByIdFileProductEntity(id));
    }

    @Override
    public void deleteAllFileProducts() {
        fileProductRepository.deleteAll();
    }

}
