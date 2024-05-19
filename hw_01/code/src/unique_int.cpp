#include <iostream>
#include <fstream>
#include <set>
#include <vector>
#include <string>
#include <algorithm>
#include <filesystem>

// Function to check if a string is a valid integer
bool isValidInteger(const std::string& str) {
    if (str.empty()) return false;
    char* end;
    long val = std::strtol(str.c_str(), &end, 10);
    if (*end != '\0') return false;
    if (val < -1023 || val > 1023) return false;
    return true;
}

// Function to process the input file and generate the output file
void processFile(const std::string& inputFilePath, const std::string& outputFilePath) {
    std::ifstream infile(inputFilePath);
    std::ofstream outfile(outputFilePath);
    std::set<int> uniqueIntegers;

    std::string line;
    while (std::getline(infile, line)) {
        // Trim leading and trailing whitespaces
        line.erase(0, line.find_first_not_of(" \t"));
        line.erase(line.find_last_not_of(" \t") + 1);

        if (isValidInteger(line)) {
            uniqueIntegers.insert(std::stoi(line));
        }
    }

    // Write sorted unique integers to the output file
    for (const int& num : uniqueIntegers) {
        outfile << num << std::endl;
    }
}

int main() {
    std::string inputDirectory = "/dsa/hw01/sample_inputs/";
    std::string outputDirectory = "/dsa/hw01/sample_results/";

    // Create output directory if it doesn't exist
    std::filesystem::create_directories(outputDirectory);

    for (const auto& entry : std::filesystem::directory_iterator(inputDirectory)) {
        std::string inputFilePath = entry.path().string();
        std::string outputFilePath = outputDirectory + entry.path().filename().string() + "_results.txt";
        processFile(inputFilePath, outputFilePath);
    }

    return 0;
}
